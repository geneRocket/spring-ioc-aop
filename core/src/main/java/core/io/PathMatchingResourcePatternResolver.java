package core.io;

import core.util.AntPathMatcher;
import core.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PathMatchingResourcePatternResolver {
    public static String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public Resource[] getResources(String locationPattern) throws IOException{
        if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)){
            if (this.pathMatcher.isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
                // a class path resource pattern
                return findPathMatchingResources(locationPattern);
            }
            else {
                // all class path resources with the given name
                return findAllClassPathResources(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()));
            }
        }
        else {
            return findAllClassPathResources(locationPattern);
        }
    }

    protected Resource[] findAllClassPathResources(String location) throws IOException {
        String path = location;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Set<Resource> result = doFindAllClassPathResources(path);
        return result.toArray(new Resource[result.size()]);
    }

    protected Set<Resource> doFindAllClassPathResources(String path) throws IOException {
        Set<Resource> result = new LinkedHashSet<>(16);
//        path="core/io/";
        Enumeration<URL> resourceUrls = this.getClass().getClassLoader().getResources(path);
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            result.add(new ClassPathResource(url.getPath()));
        }
        return result;
    }

    protected String determineRootDir(String location) {
        int prefixEnd = location.indexOf(":") + 1;
        int rootDirEnd = location.length();
        while (rootDirEnd > prefixEnd && pathMatcher.isPattern(location.substring(prefixEnd, rootDirEnd))) {
            rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
        }
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        return location.substring(0, rootDirEnd);
    }

    protected Resource[] findPathMatchingResources(String locationPattern) throws IOException {
        String rootDirPath = determineRootDir(locationPattern);
        String subPattern = locationPattern.substring(rootDirPath.length());
        Resource[] rootDirResources = getResources(rootDirPath);
        Set<Resource> result = new LinkedHashSet<>(16);
        for (Resource rootDirResource : rootDirResources) {

                result.addAll(doFindPathMatchingFileResources(rootDirResource, subPattern));

        }

        return result.toArray(new Resource[result.size()]);
    }

    protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern) {
        Set<Resource> result = new LinkedHashSet<>();
        try {
            doRetrieveMatchingFiles(subPattern,new File(rootDirResource.getPath()),result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<Resource> result) throws IOException {

        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            return;
        }
        Arrays.sort(dirContents);
        for (File content : dirContents) {
            String currPath = content.getAbsolutePath();

            if (this.pathMatcher.match(fullPattern, currPath)) {
                result.add(new ClassPathResource(content.getPath()));
            }
        }
    }
}

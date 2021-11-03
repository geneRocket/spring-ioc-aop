package context.annotation;

import beans.factory.config.BeanDefinition;
import beans.factory.config.BeanDefinitionHolder;
import beans.factory.support.BeanDefinitionRegistry;
import beans.BeansException;
import core.io.PathMatchingResourcePatternResolver;
import core.io.Resource;
import core.type.AnnotationTypeFilter;
import core.type.SimpleMetadataReader;
import core.type.classreading.MetadataReader;
import strereotype.Component;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClassPathBeanDefinitionScanner {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private PathMatchingResourcePatternResolver resourcePatternResolver= new PathMatchingResourcePatternResolver();
    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    private final List<AnnotationTypeFilter> includeFilters = new LinkedList<>();

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
        registerDefaultFilters();
    }

    protected void registerDefaultFilters() {
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }


    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {

                String beanName = null;
                try {
                    beanName = candidate.getBeanClass().getName();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);

                beanDefinitions.add(definitionHolder);
                this.registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

            }
        }
        return beanDefinitions;
    }

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        try {
            return scanCandidateComponents(basePackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Set<BeanDefinition> scanCandidateComponents(String basePackage) throws Exception {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            String packageSearchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    basePackage + '/' + this.resourcePattern;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);

            for (Resource resource : resources) {
                try {
                    MetadataReader metadataReader = new SimpleMetadataReader(resource);
                    if (isCandidateComponent(metadataReader)) {
                        candidates.add(new BeanDefinition(metadataReader.getClassName()));
                    }

                }
                catch (Throwable ex) {
                    throw new Exception(
                            "Failed to read candidate component class: " + resource, ex);
                }


            }
        }
        catch (IOException ex) {
            throw new BeansException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        for (AnnotationTypeFilter tf : this.includeFilters) {
            if (tf.matchSelf(metadataReader)) {
                return true;
            }
        }
        return false;
    }


}

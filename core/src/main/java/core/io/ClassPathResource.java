package core.io;


import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private final String path;
    private ClassLoader classLoader;


    public ClassPathResource(String path) {
        this.path = path;
        this.classLoader=Thread.currentThread().getContextClassLoader();
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return this.classLoader.getResourceAsStream(this.path);
    }
}

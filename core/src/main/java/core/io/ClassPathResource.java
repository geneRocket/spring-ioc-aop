package core.io;


import java.io.FileInputStream;
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

        InputStream inputStream= this.classLoader.getResourceAsStream(this.path);
        if(inputStream==null){
            inputStream=new FileInputStream(this.path);
        }
        if(inputStream==null){
            throw new IOException("path error");
        }

        return inputStream;
    }

    @Override
    public String getPath() {
        return this.path;
    }
}

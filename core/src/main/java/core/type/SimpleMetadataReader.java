package core.type;

import core.asm.ClassReader;
import core.io.Resource;
import core.type.classreading.MetadataReader;
import core.util.ClassUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleMetadataReader implements MetadataReader {
    String className;
    private final AnnotationMetadataReadingVisitor annotationMetadata;
    private final Resource resource;

    public SimpleMetadataReader(Resource resource) throws Exception {
        InputStream is = new BufferedInputStream(resource.getInputStream());
        ClassReader classReader;
        try {
            classReader = new ClassReader(is);
        }
        catch (IllegalArgumentException ex) {
            throw new Exception("ASM ClassReader failed to parse class file - " +
                    "probably due to a new Java class file version that isn't supported yet: " + resource, ex);
        }
        finally {
            is.close();
        }

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);
        this.className= ClassUtils.convertResourcePathToClassName(classReader.getClassName());
        this.annotationMetadata = visitor;
        // (since AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor)
        this.resource = resource;
    }

    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }

    @Override
    public String getClassName() {
        return this.className;
    }
}

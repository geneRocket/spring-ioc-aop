package core.type.classreading;

import core.type.AnnotationMetadata;

public interface MetadataReader {
    AnnotationMetadata getAnnotationMetadata();
    String getClassName();

}

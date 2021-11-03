package core.type;

import core.type.classreading.MetadataReader;

import java.lang.annotation.Annotation;

public class AnnotationTypeFilter {

    private final Class<? extends Annotation> annotationType;

    public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public boolean matchSelf(MetadataReader metadataReader) {
        AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
        return metadata.hasAnnotation(this.annotationType.getName()) ||
                (metadata.hasMetaAnnotation(this.annotationType.getName()));
    }
}

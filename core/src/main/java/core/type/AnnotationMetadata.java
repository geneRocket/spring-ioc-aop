package core.type;

import java.util.Set;

public interface AnnotationMetadata {

    boolean hasAnnotation(String annotationName);
    boolean hasMetaAnnotation(String metaAnnotationName);
    Set<String> getMetaAnnotationTypes(String annotationName);

}

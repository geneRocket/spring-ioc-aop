package core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class AnnotationUtils {

    public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
        return annotatedElement.getAnnotations();
    }
}

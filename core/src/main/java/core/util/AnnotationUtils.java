package core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationUtils {

    public static Object getValue(Annotation annotation, String attributeName) {
        if (annotation == null || !StringUtils.hasText(attributeName)) {
            return null;
        }
        try {
            Method method = annotation.annotationType().getDeclaredMethod(attributeName);
            ReflectionUtils.makeAccessible(method);
            return method.invoke(annotation);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
        catch (InvocationTargetException ex) {
            throw new IllegalStateException("Could not obtain value for annotation attribute '" +
                    attributeName + "' in " + annotation, ex);
        }
        catch (Throwable ex) {
            return null;
        }
    }

    public static <A extends Annotation> A findAnnotation(Class<?> clazz,Class<A> annotationType) {
        if (annotationType == null) {
            return null;
        }

        A annotation = clazz.getDeclaredAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        // For backwards compatibility, perform a superclass search with plain annotations
        // even if not marked as @Inherited: e.g. a findAnnotation search for @Deprecated
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return findAnnotation(superclass, annotationType);

    }
}

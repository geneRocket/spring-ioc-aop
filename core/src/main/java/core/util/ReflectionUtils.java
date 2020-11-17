package core.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}

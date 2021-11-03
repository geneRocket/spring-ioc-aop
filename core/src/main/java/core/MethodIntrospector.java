package core;

import core.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public abstract class MethodIntrospector {

    public static <T> Map<Method, T> selectMethods(Class<?> targetType, final MetadataLookup<T> metadataLookup) {
        final Map<Method, T> methodMap = new LinkedHashMap<>();

        ReflectionUtils.doWithMethods(targetType, method -> {
            T result = metadataLookup.inspect(method);
            if (result != null) {
                methodMap.put(method, result);
            }
        });

        return methodMap;
    }

    public interface MetadataLookup<T> {

        T inspect(Method method);
    }
}

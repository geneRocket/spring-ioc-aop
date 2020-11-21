package core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClassUtils {
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static final Class<?>[] EMPTY_CLASS_ARRAY = {};

    public static Class<?> forName(String name)
            throws ClassNotFoundException, LinkageError {

        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException ex) {
            throw ex;
        }
    }

    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
        if (clazz.isInterface() ) {
            return Collections.singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<>();
        Class<?> current = clazz;
        while (current != null) {
            Class<?>[] ifcs = current.getInterfaces();
            for (Class<?> ifc : ifcs) {
                interfaces.add(ifc);
            }
            current = current.getSuperclass();
        }
        return interfaces;
    }


    public static Class<?>[] toClassArray(Collection<Class<?>> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_CLASS_ARRAY) : EMPTY_CLASS_ARRAY);
    }

    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
        return toClassArray(getAllInterfacesForClassAsSet(clazz));
    }

    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                return superclass;
            }
        }
        return clazz;
    }
}

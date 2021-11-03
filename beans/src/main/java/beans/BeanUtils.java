package beans;

import core.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class BeanUtils {
    public static <T> T instantiateClass(Class<T> clazz) throws BeansException{
        if (clazz.isInterface()) {
            throw new BeansException("Specified class is an interface");
        }
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            return instantiateClass(ctor);
        }
        catch (NoSuchMethodException ex) {
            throw new BeansException( "No default constructor found", ex);
        }
    }

    public static <T> T instantiateClass(Constructor<T> ctor, Object... args)  {
        try {
            ReflectionUtils.makeAccessible(ctor);
            return ( ctor.newInstance(args));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

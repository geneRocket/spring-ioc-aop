package core.util;

public abstract class ObjectUtils {
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }
}

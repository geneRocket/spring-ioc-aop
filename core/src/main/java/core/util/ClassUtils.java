package core.util;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {

    public static Class<?> forName(String name)
            throws ClassNotFoundException, LinkageError {

        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException ex) {
            throw ex;
        }
    }
}

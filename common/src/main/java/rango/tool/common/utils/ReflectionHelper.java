package rango.tool.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {

    private static final Map<String, Class<?>> sClasses = new HashMap<>();
    private static final Map<String, Constructor<?>> sConstructors = new HashMap<>();
    private static final Map<String, Method> sMethods = new HashMap<>();
    private static final Map<String, Field> sFields = new HashMap<>();

    public static Class<?> getClass(String klass)
            throws ClassNotFoundException {
        Class<?> reflected = sClasses.get(klass);
        if (reflected == null) {
            reflected = Class.forName(klass);
            sClasses.put(klass, reflected);
        }
        return reflected;
    }

    public static Constructor<?> getConstructor(Class<?> klass, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        String cacheKey = klass.getName() + "#" + Arrays.deepToString(parameterTypes);
        Constructor<?> reflected = sConstructors.get(cacheKey);
        if (reflected == null) {
            reflected = klass.getConstructor(parameterTypes);
            sConstructors.put(cacheKey, reflected);
        }
        return reflected;
    }

    public static Method getMethod(Class<?> klass, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        String cacheKey = klass.getName() + "#" + name + "#" + Arrays.deepToString(parameterTypes);
        Method reflected = sMethods.get(cacheKey);
        if (reflected == null) {
            reflected = klass.getMethod(name, parameterTypes);
            sMethods.put(cacheKey, reflected);
        }
        return reflected;
    }

    public static Method getDeclaredMethod(Class<?> klass, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        String cacheKey = klass.getName() + "#" + name + "#" + Arrays.deepToString(parameterTypes);
        Method reflected = sMethods.get(cacheKey);
        if (reflected == null) {
            reflected = klass.getDeclaredMethod(name, parameterTypes);
            sMethods.put(cacheKey, reflected);
        }
        return reflected;
    }

    public static Field getField(Class<?> klass, String name)
            throws NoSuchFieldException {
        Field reflected = sFields.get(name);
        if (reflected == null) {
            reflected = klass.getField(name);
            sFields.put(name, reflected);
        }
        return reflected;
    }
}

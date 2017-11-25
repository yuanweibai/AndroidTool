package rango.tool.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by baiyuanwei on 17/11/16.
 */

public class ReflectionUtils {
    private static final Map<String, Class> CLASS_MAP = new ConcurrentHashMap<>();

    public static Class<?> getClassForName(String className) {
        Class<?> ret = CLASS_MAP.get(className);

        if (ret == null) {
            try {
                ret = Class.forName(className);

                CLASS_MAP.put(className, ret);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
}

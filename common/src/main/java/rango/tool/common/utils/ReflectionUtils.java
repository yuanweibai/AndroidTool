package rango.tool.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ReflectionUtils {
    private static final Map<String, Class> CLASS_MAP = new ConcurrentHashMap<>();

    static Class<?> getClassForName() {
        Class<?> ret = CLASS_MAP.get("android.view.Display");

        if (ret == null) {
            try {
                ret = Class.forName("android.view.Display");

                CLASS_MAP.put("android.view.Display", ret);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
}

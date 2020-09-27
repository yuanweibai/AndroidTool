package rango.tool.common.rom;

import java.lang.reflect.Method;

public final class SystemProperties {
    /**
     * Gets system properties set by <code>adb shell setprop <em>key</em> <em>value</em></code>
     *
     * @param key the property key.
     * @param defaultValue the value to return if the property is undefined or empty (this parameter
     *            may be {@code null}).
     * @return the system property value or the default value.
     */
    public static String get(String key, String defaultValue) {
        try {
            final Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            final Method get = systemProperties.getMethod("get", String.class, String.class);
            return (String) get.invoke(null, key, defaultValue);
        } catch (Exception e) {
            // This should never happen
            return defaultValue;
        }
    }

    private SystemProperties() {
    }
}

package rango.tool.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Compats {

    public static final boolean IS_GOOGLE_DEVICE = "Google".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_SAMSUNG_DEVICE = "Samsung".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_HUAWEI_DEVICE = "Huawei".equalsIgnoreCase(Build.BRAND)
            || (!IS_GOOGLE_DEVICE && "Huawei".equalsIgnoreCase(Build.MANUFACTURER));

    public static final boolean IS_HTC_DEVICE = "HTC".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_HISENSE_DEVICE = "Hisense".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_GIONEE_DEVICE = "Gionee".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_LGE_DEVICE = "LGE".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_SONY_DEVICE = "Sony".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_MOTOROLA_DEVICE = "Motorola".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_LENOVO_DEVICE = "Lenovo".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_ZTE_DEVICE = "Zte".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_MEIZU_DEVICE = "Meizu".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_XIAOMI_DEVICE = "Xiaomi".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_TCL_DEVICE = "TCL".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_VIVO_DEVICE = "Vivo".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_OPPO_DEVICE = "OPPO".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_SMARTISAN_DEVICE = "SMARTISAN".equalsIgnoreCase(Build.BRAND);

    public static final boolean IS_RED_MI_NOTE_7 = "Redmi Note 7".equalsIgnoreCase(Build.MODEL);

    public static final boolean IS_CHERRY_MOBILE = "Cherry".equalsIgnoreCase(Build.BRAND)
            || "Cherry_Mobile".equalsIgnoreCase(Build.BRAND)
            || "Cherry Mobile".equalsIgnoreCase(Build.BRAND)
            || "CherryMobile".equalsIgnoreCase(Build.BRAND);

    public static final String SAMSUNG_SM_G9500 = "SM-G9500";
    private static final String GOOGLE_NEXUS_5 = "Nexus 5";
    private static final String MOTOROLA_MOTOE2 = "MotoE2(4G-LTE)";

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version"; // 软件版本号
    private static final String KEY_VERSION_VIVO_DISPLAY_ID = "ro.vivo.os.build.display.id"; // vivo Rom

    /**
     * Huawei EMUI devices.
     */
    public static class EmuiBuild {
        private static final String BUILD_PROP_NAME = "ro.build.hw_emui_api_level";

        public static class VERSION_CODES {
            /**
             * EMUI 4.0 (API 23).
             */
            public static final int EMUI_4_0 = 9;

            /**
             * EMUI 4.1 (API 23).
             */
            public static final int EMUI_4_1 = 10;

            /**
             * EMUI 5.0 (API 24).
             */
            public static final int EMUI_5_0 = 11;
        }
    }

    /**
     * Only works for EMUI 4.0 or above.
     */
    @SuppressLint("PrivateApi")
    public static int getHuaweiEmuiVersionCode() {
        int versionCode = 0;
        String versionStr = getRomVersionCode(EmuiBuild.BUILD_PROP_NAME);
        if (!TextUtils.isEmpty(versionStr) && TextUtils.isDigitsOnly(versionStr)) {
            try {
                versionCode = Integer.parseInt(versionStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return versionCode;
    }

    @SuppressLint("PrivateApi")
    public static String getHuaweiEmuiVersionName() {
        Class<?> classType;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, "ro.build.version.emui");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isUpNavigationBarHeight() {
        return !(IS_SAMSUNG_DEVICE && Build.MODEL.equals(SAMSUNG_SM_G9500));
    }

    public static boolean isCouldImmerse() {
        return !((IS_GOOGLE_DEVICE && Build.MODEL.equals(GOOGLE_NEXUS_5))
                || IS_MOTOROLA_DEVICE && Build.MODEL.equals(MOTOROLA_MOTOE2));
    }

    public static String getOppoVersionName() {
        return getRomVersionCode(KEY_VERSION_OPPO);
    }

    public static String getVivoVersionName() {
        return getRomVersionCode(KEY_VERSION_VIVO);
    }

    public static String getMiuiVersionName() {
        return getRomVersionCode(KEY_VERSION_MIUI);
    }

    // for
    public static boolean isFuntouchLite() {
        String rom = getRomVersionCode(KEY_VERSION_VIVO_DISPLAY_ID);
        return TextUtils.isEmpty(rom) && (rom.contains("Lite") || rom.contains("lite"));
    }

    @NonNull
    private static String getRomVersionCode(String name) {
        String line = "";
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (line == null) {
            return "";
        }
        return line;

    }
}

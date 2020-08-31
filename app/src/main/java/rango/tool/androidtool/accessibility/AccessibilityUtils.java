package rango.tool.androidtool.accessibility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;

public class AccessibilityUtils {

    public static final boolean IS_GOOGLE_DEVICE = "Google".equalsIgnoreCase(Build.BRAND);
    public static final boolean IS_HUAWEI_DEVICE = "Huawei".equalsIgnoreCase(Build.BRAND)
            || (!IS_GOOGLE_DEVICE && "Huawei".equalsIgnoreCase(Build.MANUFACTURER));

    /**
     * 开启 Accessibility 权限
     */
    public static void gotoAcc(){
        ToolApplication.getContext().startActivity(getAccessibilitySettingsIntent());
    }

    /**
     * 判断是否已经开启了 Accessibility 权限
     */
    public static boolean isAccessibilityGranted() {
        boolean isGranted = false;
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(ToolApplication.getContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(ToolApplication.getContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                isGranted = services.toLowerCase().contains(ToolApplication.getContext().getPackageName().toLowerCase());
            }
        }
        return isGranted;
    }

    @NonNull
    private static Intent getAccessibilitySettingsIntent() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        String str = "com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment";
        if ("Xiaomi".equalsIgnoreCase(Build.BRAND)
                && (Build.VERSION.SDK_INT > 19 || (hasClassInApk(ToolApplication.getContext(), "com.android.settings", str) && Build.VERSION.SDK_INT == 19))) {
            intent.setAction("android.intent.action.MAIN");
            String packageName = ToolApplication.getContext().getPackageName();
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
            intent.putExtra(":android:show_fragment_short_title", 0);
            intent.putExtra(":android:show_fragment_args", 0);
            intent.putExtra(":android:show_fragment_title", 0);
            intent.putExtra(":android:no_headers", true);
            intent.putExtra("setting:ui_options", 1);
            Bundle bundle = new Bundle();
            bundle.putString("summary", ToolApplication.getContext().getString(R.string.accessibility_service_description));
            bundle.putString("title", ToolApplication.getContext().getString(R.string.app_name));
            bundle.putString("preference_key", packageName + "/" + ToolAccessibilityService.class.getName());
            bundle.putParcelable("component_name", new ComponentName(packageName, ToolAccessibilityService.class.getName()));
            bundle.putBoolean("checked", false);
            intent.putExtra(":android:show_fragment", str);
            intent.putExtra(":android:show_fragment_args", bundle);
        } else if (IS_HUAWEI_DEVICE) {
            intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        } else {
            intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        }
        return intent;
    }

    private static boolean hasClassInApk(Context context, String str, String str2) {
        boolean z = false;
        try {
            Context createPackageContext = context.createPackageContext(str, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            if (createPackageContext != null) {
                Class loadClass = createPackageContext.getClassLoader().loadClass(str2);
                if (loadClass != null) {
                    z = true;
                }
            }
        } catch (Exception ignore) {
        }
        return z;
    }
}

package rango.tool.common.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;

import rango.tool.common.utils.Navigations;

public class VivoUtils {


//    /**
//     * Vivo 自启动权限申请
//     */
//    public static boolean goToVivoPermissionAutoStartActivity(Context context) {
//        Intent intent = new Intent();
//        ComponentName componentName = ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.PurviewTabActivity");
//        intent.setComponent(componentName);
//
//        if (isIntentAvailable(intent, context)) {
//            Navigations.startActivitySafely(context, intent);
//            return true;
//        }
//
//        intent = new Intent();
//        componentName = ComponentName.unflattenFromString("com.iqoo.secure/com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
//        intent.setComponent(componentName);
//
//        if (isIntentAvailable(intent, context)) {
//            Navigations.startActivitySafely(context, intent);
//            return true;
//        }
//
//        try {
//            Navigations.startSystemSetting(context, Settings.ACTION_SETTINGS, true);
//        } catch (Exception e) {
//            Log.e("Permissions", Log.getStackTraceString(e));
//        }
//        return true;
//
//    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static boolean goToVivoPermissionShowOnLockScreenActivity(Context context) {
        Intent intent = new Intent();
        ComponentName componentName = ComponentName.unflattenFromString("com.iqoo.secure/.MainActivity");
        intent.setComponent(componentName);

        if (isIntentAvailable(intent, context)) {
            Navigations.startActivitySafely(context, intent);
            return true;
        }

        try {
            Navigations.startSystemSetting(context, Settings.ACTION_SETTINGS, true);
        } catch (Exception e) {
            Log.e("Permissions", Log.getStackTraceString(e));
        }

        return true;
    }
}

package rango.tool.common.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;

import rango.tool.common.utils.Navigations;

public class OppoUtils {

    private static final String TAG = "OppoUtils";

    /**
     * 检测 360 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            Log.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * oppo ROM 权限申请
     */
    public static void applyOppoPermission(Context context) {
        //merge request from https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //com.coloros.safecenter/.sysfloatwindow.FloatWindowListActivity
            ComponentName comp = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

//    /**
//     * Oppo 自启动权限申请
//     */
//    public static boolean goToOppoPermissionAutoStartActivity(Context context) {
//        Intent intent = new Intent();
//        ComponentName componentName = null;
//        componentName = ComponentName.unflattenFromString("com.coloros.safecenter/.startupapp.StartupAppListActivity");
//        intent.setComponent(componentName);
//        if (isIntentAvailable(intent, context)) {
//            Navigations.startActivitySafely(context, intent);
//            return true;
//        }
//
//        componentName = ComponentName.unflattenFromString("com.color.safecenter/.permission.startup.StartupAppListActivity");
//        intent.setComponent(componentName);
//
//        if (isIntentAvailable(intent, context)) {
//            Navigations.startActivitySafely(context, intent);
//            return true;
//        }
//
//        componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity");
//        intent.setComponent(componentName);
//
//        if (isIntentAvailable(intent, context)) {
//            Navigations.startActivitySafely(context, intent);
//            return true;
//        }
//
//        componentName = ComponentName.unflattenFromString("com.color.safecenter/.permission.startup.StartupAppListActivity");
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
}

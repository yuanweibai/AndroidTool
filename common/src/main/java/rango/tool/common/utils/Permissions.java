package rango.tool.common.utils;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.Set;

import rango.tool.common.rom.HuaweiUtils;
import rango.tool.common.rom.MiuiUtils;
import rango.tool.common.rom.OppoUtils;
import rango.tool.common.rom.QikuUtils;
import rango.tool.common.rom.RomUtils;
import rango.tool.common.rom.VivoUtils;

import static android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION;

@SuppressWarnings("unused")
public class Permissions {
    private static final String TAG = Permissions.class.getSimpleName();

    @SuppressLint("NewApi")
    public static boolean isFloatWindowAllowed(Context context) {
        if (RomUtils.checkIsVivoRom()) {
            // can't find how to check vivo permission
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (RomUtils.checkIsMiuiRom()) {
                return MiuiUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return HuaweiUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIs360Rom()) {
                return QikuUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return OppoUtils.checkFloatWindowPermission(context);
            } else {
                return true;
            }
        }
        if (Compats.getHuaweiEmuiVersionCode() == Compats.EmuiBuild.VERSION_CODES.EMUI_4_1) {
            return true;
        }

        boolean canDrawOverlays = false;
        try {
            canDrawOverlays = Settings.canDrawOverlays(context);
        } catch (RuntimeException e) {
            //
        }

        return canDrawOverlays;
    }

//    public static boolean hasPermission(String permission) {
//        boolean granted = false;
//        if (!TextUtils.isEmpty(permission)) {
//            try {
//                granted = ContextCompat.checkSelfPermission(HSApplication.getContext(), permission)
//                        == PackageManager.PERMISSION_GRANTED;
//            } catch (RuntimeException e) {
//            }
//        }
//        return granted;
//    }

//    public static boolean isUsageAccessGranted() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            return true;
//        }
//        Context context = HSApplication.getContext();
//        try {
//            PackageManager pm = context.getPackageManager();
//            ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), 0);
//            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            if (appOpsManager == null) {
//                return false;
//            }
//            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
//                    applicationInfo.uid, applicationInfo.packageName);
//            return (mode == AppOpsManager.MODE_ALLOWED);
//        } catch (Exception e) {
//            return false;
//        }
//    }

//    public static boolean isNotificationAccessGranted() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            return false;
//        }
//
//        Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(HSApplication.getContext());
//        return enabledPackages.contains(HSApplication.getContext().getPackageName());
//    }


    public static void requestFloatWindowPermission(Context context) {
        if (RomUtils.checkIsMiuiRom()) {
            MiuiUtils.applyMiuiPermission(context);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (RomUtils.checkIsHuaweiRom()) {
                HuaweiUtils.applyPermission(context);
            } else if (RomUtils.checkIs360Rom()) {
                QikuUtils.applyPermission(context);
            } else if (RomUtils.checkIsOppoRom()) {
                OppoUtils.applyOppoPermission(context);
            }
        } else {
            try {
                Navigations.startSystemSetting(context, ACTION_MANAGE_OVERLAY_PERMISSION, true);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

//    public static void requestAutoStartPermission(Context context) {
//        if (RomUtils.checkIsMiuiRom()) {
//            MiuiUtils.goToMiuiPermissionAutoStartActivity(context);
//        } else if (RomUtils.checkIsOppoRom()) {
//            OppoUtils.goToOppoPermissionAutoStartActivity(context);
//        } else if (RomUtils.checkIsVivoRom()) {
//            VivoUtils.goToVivoPermissionAutoStartActivity(context);
//        } else if (RomUtils.checkIsHuaweiRom()) {
//            HuaweiUtils.goToHuaweiPermissionAutoStartActivity(context);
//        } else {
//            try {
//                Navigations.startSystemSetting(context, Settings.ACTION_SETTINGS, true);
//            } catch (Exception e) {
//                Log.e(TAG, Log.getStackTraceString(e));
//            }
//        }
//    }

    public static void requestShowOnLockScreenPermission(Context context) {
        if (RomUtils.checkIsMiuiRom()) {
            MiuiUtils.goToMiuiPermissionShowOnLockScreenActivity(context);
        } else if (RomUtils.checkIsVivoRom()) {
            VivoUtils.goToVivoPermissionShowOnLockScreenActivity(context);
        } else {
            try {
                Navigations.startSystemSetting(context, Settings.ACTION_SETTINGS, true);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

}

package rango.tool.androidtool.notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import java.util.Set;

import rango.kotlin.mytest.TransparentActivity;
import rango.tool.androidtool.experiments.activity.WindowActivity;
import rango.tool.common.utils.Worker;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ToolNotificationListenerService extends NotificationListenerService {

    private static final String TAG = "TooNotification";

    public static void openNotificationListenSettings(Activity activity) {
        if (isNotificationListenerEnabled(activity)) {
            return;
        }
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, ToolNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, ToolNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        NotificationInfoBean infoBean = NotificationInfoBean.valueOf(sbn);
        Log.e(TAG, "onNotificationPosted:" + infoBean.title);

        Log.e("rango-weixin", "posted---packageId: " + infoBean.packageId + ",\n text: " + infoBean.text + ", \ntitle: " + infoBean.title);

        if (("语音通话中").equals(infoBean.text)) {
            Worker.postMain(new Runnable() {
                @Override
                public void run() {
                    WindowActivity.showWindow();
                }
            }, 4000);
        }

//        Worker.postMain(() -> {
//            if (infoBean.text.equals("栖梧")) {
//                TransparentActivity.start();
//            }
//        }, 2000);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.e(TAG, "onNotificationRemoved:" + sbn.toString());
        NotificationInfoBean infoBean = NotificationInfoBean.valueOf(sbn);

        Log.e("rango-weixin", "removed---packageId: " + infoBean.packageId + ",\n text: " + infoBean.text + ", \ntitle: " + infoBean.title);

        if (infoBean.title.equals("栖梧")) {
            TransparentActivity.stop();
        }
    }

    @Override
    public void onListenerConnected() {
        Log.e(TAG, "onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        Log.e(TAG, "onListenerDisconnected");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    private static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            Log.e(TAG, "isNotificationListenerEnabled: true");
            return true;
        }
        Log.e(TAG, "isNotificationListenerEnabled: false");
        return false;
    }
}

package rango.tool.androidtool.alive.frontdeskservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;

/**
 * 7.0 以下的手机可以通过此方案取消前台 service 的可见通知
 */
public class CancelNotificationService extends Service {

    private static final String TAG = "CancelNotification";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopSelf();
            return START_NOT_STICKY;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("FrontDeskService");
            builder.setContentText("Service is running...");

            // 覆盖 NOTIFICATION_ID 的通知
            startForeground(FrontDeskService.NOTIFICATION_ID, builder.build());

            new Thread(new Runnable() {
                @Override public void run() {

                    // 取消CancelNotificationService的通知
                    stopForeground(true);

                    stopSelf();
                }
            }).start();
        } else {
            startForeground(FrontDeskService.NOTIFICATION_ID, new Notification());

            new Thread(new Runnable() {
                @Override public void run() {
                    Log.e(TAG, "start cancel notification ......");
//                    SystemClock.sleep(10000);

                    // 取消CancelNotificationService的通知
                    stopForeground(true);

                    Log.e(TAG, "cancel notification successfully!!!");
                    stopSelf();
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        super.onDestroy();
    }
}

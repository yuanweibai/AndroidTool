package rango.tool.androidtool.alive.frontdeskservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;

public class FrontDeskService extends Service {

    private static final String TAG = "FrontDeskService";
    public static final int NOTIFICATION_ID = 1001;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        return null;
    }

    @Override public void onCreate() {
        Log.e(TAG, "onCreate()");
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "com.rango.androidtool.frontDeskNotification";
            NotificationChannel channel = new NotificationChannel(channelId, "rango", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }

            Notification notification = new Notification.Builder(this, channelId)
                    .setSmallIcon(R.drawable.test_3)
                    .setContentTitle("Rango")
                    .setContentText("Running....")
                    .build();
            startForeground(NOTIFICATION_ID, notification);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("FrontDeskService");
            builder.setContentText("Service is running...");
            startForeground(NOTIFICATION_ID, builder.build());

            Intent intent = new Intent(this, CancelNotificationService.class);
            startService(intent);
        } else {
            startForeground(NOTIFICATION_ID, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        // 如果Service 被终止
        // 当资源允许的情况下，重启Service
        return START_STICKY;
    }

    @Override public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancel(NOTIFICATION_ID);
            }
        }

        startService(new Intent(ToolApplication.getContext(), FrontDeskService.class));
    }
}

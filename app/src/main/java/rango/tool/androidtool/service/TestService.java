package rango.tool.androidtool.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import android.util.Log;

import java.util.List;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.common.utils.Worker;

public class TestService extends Service {

    private static final String TAG = TestService.class.getSimpleName();

    public TestService() {
    }

    private static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static void bindService() {
        Context context = ToolApplication.getContext();
        Intent intent = new Intent(context, TestService.class);
        context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public static void unBindService() {
        ToolApplication.getContext().unbindService(serviceConnection);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand(), flags = " + flags + ", startId = " + startId + ",ThreadId = " + Thread.currentThread().getId());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(startId, new Notification());
//        }
        startTask();

        makeServiceForeground();

        return START_STICKY;
    }

    private void startTask() {
        Worker.postWorker(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        Log.e("BackgroundThread", "running......" + ",ThreadId = " + Thread.currentThread().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()," + ",ThreadId = " + Thread.currentThread().getId());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(1, new Notification());
//        }
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e(TAG, "onRebind()");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnBind()," + ",ThreadId = " + Thread.currentThread().getId());
        return true;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void makeServiceForeground() {

        NotificationChannel notificationChannel = new NotificationChannel(
                "dddddd",
                "cccccccccccc",
                NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        notificationManager.createNotificationChannel(notificationChannel);

        startForeground(12, buildDefaultNotification(this));
    }

    private Notification buildDefaultNotification(Context context) {
        String title = "ddddcccc";
        String content = "test..........";
        Notification.Builder builder = new Notification.Builder(context, "dddddd");
        builder.setContentTitle(title).setContentText(content)
                .setSmallIcon(R.drawable.coin_icon);
        return builder.build();
    }

    private static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;

        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm == null) return false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (!pm.isInteractive()) return false;
        } else {
            if (!pm.isScreenOn()) return false;
        }

        String packageName = context.getApplicationContext().getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance
                    == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }

        }
        return false;
    }

    public static boolean needMakeServiceForeground(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isAppOnForeground(context);
    }
}

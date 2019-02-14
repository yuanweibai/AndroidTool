package rango.tool.androidtool.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.ToolApplication;
import rango.tool.common.utils.Worker;

public class TestService extends Service {

    private static final String TAG = TestService.class.getSimpleName();

    public TestService() {
    }

    private static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override public void onServiceDisconnected(ComponentName name) {

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
        Log.e(TAG, "onStartCommand(), flags = " + flags + ", startId = " + startId + ",ThreadId = " + Thread.currentThread().getId());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(startId, new Notification());
//        }
        startTask();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTask() {
        Worker.postWorker(new Runnable() {
            @Override public void run() {
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

    @Override public void onDestroy() {
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
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnBind()," + ",ThreadId = " + Thread.currentThread().getId());
        return super.onUnbind(intent);
    }
}

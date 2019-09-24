package rango.tool.androidtool.alive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();
    private boolean isCreated = false;
    private volatile boolean isCancel = false;

    @Override
    public void onCreate() {
        super.onCreate();
        isCreated = false;
        AliveManager.getInstance().setDownloadServiceAlive(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isCreated) {
            isCreated = true;
            isCancel = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    try {
                        while (!isCancel) {
                            Log.e(TAG, "--------------- download...");
                            Thread.sleep(3000);
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            }).start();
        }

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCancel = true;
        AliveManager.getInstance().setDownloadServiceAlive(false);
    }
}

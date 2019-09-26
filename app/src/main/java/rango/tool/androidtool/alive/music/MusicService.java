package rango.tool.androidtool.alive.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;

public class MusicService extends Service {

    private static final String TAG = "MusicService";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");

        mediaPlayer = MediaPlayer.create(ToolApplication.getContext(), R.raw.bg_sea);
        mediaPlayer.setLooping(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                startMusic();
            }
        }).start();
        return START_STICKY;
    }

    private void startMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        stopMusic();

        startService(new Intent(ToolApplication.getContext(), MusicService.class));
    }
}

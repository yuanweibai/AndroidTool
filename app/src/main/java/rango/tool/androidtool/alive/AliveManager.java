package rango.tool.androidtool.alive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.alive.frontdeskservice.FrontDeskService;
import rango.tool.androidtool.alive.music.MusicService;
import rango.tool.androidtool.alive.onepixel.OnePixelActivity;

public class AliveManager {

    private WeakReference<Activity> onePixelActivityRef;

    private AliveManager() {
    }

    private static class ClassHolder {
        private static final AliveManager INSTANCE = new AliveManager();
    }

    public static AliveManager getInstance() {
        return ClassHolder.INSTANCE;
    }

    private boolean isDownloadServiceAlive = false;

    public void setDownloadServiceAlive(boolean isAlive) {
        this.isDownloadServiceAlive = isAlive;
    }

    public boolean isDownloadServiceAlive() {
        return isDownloadServiceAlive;
    }

    public void setStartOnePixelAlive() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        ToolApplication.getContext().registerReceiver(new ScreenReceiver(), filter);
    }

    public void setOnePixelActivityRef(Activity activity) {
        onePixelActivityRef = new WeakReference<>(activity);
    }

    public void onScreenOn() {
        destroyOnePixelActivity();
    }

    public void onScreenOff() {
        startOnePixelActivity();
    }

    public void startMusicService() {
        ToolApplication.getContext().startService(new Intent(ToolApplication.getContext(), MusicService.class));
    }

    public void startFrontDeskService() {
        ToolApplication.getContext().startService(new Intent(ToolApplication.getContext(), FrontDeskService.class));
    }

    private void startOnePixelActivity() {
        Intent intent = new Intent(ToolApplication.getContext(), OnePixelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ToolApplication.getContext().startActivity(intent);
    }

    private void destroyOnePixelActivity() {
        if (onePixelActivityRef != null) {
            Activity activity = onePixelActivityRef.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    private class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            switch (action) {
                case Intent.ACTION_SCREEN_OFF:
                    onScreenOff();
                    break;
                case Intent.ACTION_SCREEN_ON:
                    onScreenOn();
                    break;
                default:
                    break;
            }
        }
    }
}

package rango.tool.androidtool.alive.onepixel;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import rango.tool.androidtool.alive.AliveManager;
import rango.tool.androidtool.alive.DownloadService;
import rango.tool.androidtool.base.BaseActivity;

public class OnePixelActivity extends BaseActivity {

    private static final String TAG = OnePixelActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

        AliveManager.getInstance().setOnePixelActivityRef(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged(), download service alive: " + AliveManager.getInstance().isDownloadServiceAlive());

        restartDownloadServiceIfNeeded();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy(), download service alive: " + AliveManager.getInstance().isDownloadServiceAlive());
        super.onDestroy();

        restartDownloadServiceIfNeeded();
    }

    @Override protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    @Override protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart()");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }


    private void restartDownloadServiceIfNeeded() {
        if (!AliveManager.getInstance().isDownloadServiceAlive()) {
            startService(new Intent(OnePixelActivity.this, DownloadService.class));
        }
    }
}

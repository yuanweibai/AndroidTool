package rango.tool.androidtool.launchmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class LaunchMode3Activity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("rango3", "onCreate()");
        setContentView(R.layout.activity_launch_mode_3_layout);

        findViewById(R.id.btn_1).setOnClickListener(v -> startActivity(LaunchMode1Activity.class));

        finish();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("rango3", "onNewIntent()");
    }

    @Override protected void onStart() {
        super.onStart();
        Log.e("rango3", "onStart()");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.e("rango3", "onResume()");
    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.e("rango3", "onRestart()");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.e("rango3", "onPause()");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.e("rango3", "onStop()");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.e("rango3", "onDestroy()");
    }

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("rango3", "onAttachedToWindow()");
    }

    @Override public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("rango3", "onDetachedFromWindow()");
    }
}

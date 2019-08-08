package rango.tool.androidtool.launchmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class LaunchMode2Activity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("rango2", "onCreate()");
        setContentView(R.layout.activity_launch_mode_2_layout);

        findViewById(R.id.btn_1).setOnClickListener(v -> LaunchMode1Activity.start(LaunchMode2Activity.this, "LaunchMode2Activity"));
        findViewById(R.id.btn_3).setOnClickListener(v -> {
            Intent intent = new Intent(LaunchMode2Activity.this, LaunchMode3Activity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("rango2", "onNewIntent()");
    }

    @Override protected void onStart() {
        super.onStart();
        Log.e("rango2", "onStart()");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.e("rango2", "onResume()");

    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.e("rango2", "onRestart()");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.e("rango2", "onPause()");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.e("rango2", "onStop()");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.e("rango2", "onDestroy()");
    }

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("rango2", "onAttachedToWindow()");
    }

    @Override public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("rango2", "onDetachedFromWindow()");
    }
}

package rango.tool.androidtool.launchmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class LaunchMode1Activity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("rango", "onCreate");
        setContentView(R.layout.activity_launch_mode_1_layout);

        findViewById(R.id.btn_1).setOnClickListener(v -> {
            startActivity(LaunchMode2Activity.class);
        });
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("rango", "onNewIntent()");
    }


    @Override protected void onStart() {
        super.onStart();
        Log.e("rango", "onStart()");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.e("rango", "onResume()");
    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.e("rango", "onRestart()");
    }
}

package rango.tool.androidtool.experiments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.experiments.activity.CanvasActivity;
import rango.tool.androidtool.experiments.activity.ShapeActivity;
import rango.tool.androidtool.experiments.activity.WindowActivity;
import rango.tool.androidtool.locker.LockerManager;

public class TestActivity extends BaseActivity {

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
//                LockerManager.getInstance(TestActivity.this).lockScreen();
            } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
//                unLockScreen();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastReceiver, intentFilter);

        setContentView(R.layout.test_layout);
        findViewById(R.id.canvas_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CanvasActivity.class);
            }
        });
        findViewById(R.id.shape_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShapeActivity.class);
            }
        });

        findViewById(R.id.locker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockerManager.getInstance(TestActivity.this).lockScreen();
            }
        });
        findViewById(R.id.window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(WindowActivity.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}

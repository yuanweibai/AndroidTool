package rango.tool.androidtool.experiments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.experiments.activity.CanvasActivity;
import rango.tool.androidtool.experiments.activity.ShapeActivity;
import rango.tool.androidtool.locker.LockerManager;
import rango.tool.common.utils.TimeUtills;

public class TestActivity extends BaseActivity {

    private TextView textView;
    private TextView timeText;
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
        textView = findViewById(R.id.current_mills);
        timeText = findViewById(R.id.current_time);

        findViewById(R.id.mills_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(String.valueOf(System.currentTimeMillis()));
            }
        });

        findViewById(R.id.time_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = TimeUtills.milllsToStr(System.currentTimeMillis());
                timeText.setText(time);
            }
        });

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
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}

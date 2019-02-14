package rango.tool.androidtool.experiments.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class BroadcastActivity extends BaseActivity {

    private static String ACTION = "com.example.normal.receiver";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_layout);

        findViewById(R.id.send_broadcast_btn).setOnClickListener(v -> {
            Intent intent = new Intent(ACTION);
            sendBroadcast(intent, "android.tool.permission.TEST_BROADCAST_RECEIVER");
        });
    }
}

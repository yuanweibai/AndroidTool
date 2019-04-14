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
//            Intent intent = new Intent(ACTION);
//            sendBroadcast(intent, "android.tool.permission.TEST_BROADCAST_RECEIVER");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
//            intent.addCategory("android.intent.category.HOME");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//            intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
        });
    }
}

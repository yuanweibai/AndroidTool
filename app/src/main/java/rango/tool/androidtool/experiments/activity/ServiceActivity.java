package rango.tool.androidtool.experiments.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.service.MyIntentService;
import rango.tool.androidtool.service.MyService;
import rango.tool.androidtool.service.TestService;
import rango.tool.androidtool.workmanager.ToolWorkManager;
import rango.tool.common.utils.Worker;

public class ServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_layout);


        findViewById(R.id.start_service_btn).setOnClickListener(v -> {
            Intent intent = new Intent(ServiceActivity.this, TestService.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(intent);
//
//            } else {
            startService(intent);
//            }

        });
        findViewById(R.id.stop_service_btn).setOnClickListener(v -> {
            Intent intent = new Intent(ServiceActivity.this, TestService.class);
            stopService(intent);
        });

        findViewById(R.id.bind_service_btn).setOnClickListener(v -> TestService.bindService());
        findViewById(R.id.unbind_service_btn).setOnClickListener(v -> TestService.unBindService());
        findViewById(R.id.bind_intent_service_btn).setOnClickListener(v -> MyService.bindService());

        findViewById(R.id.delay_start_service_btn).setOnClickListener((View v) -> {
            Log.e("OneTimeWorker", "delay start......");
            ToolWorkManager.getInstance().testDelay();
        });

        findViewById(R.id.background_thread_btn).setOnClickListener((View v) -> {
            Log.e("BackgroundThread", "start......" + ",ThreadId = " + Thread.currentThread().getId());
            Worker.postWorker(new Runnable() {
                @Override public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                            Log.e("BackgroundThread", "running......" + ",ThreadId = " + Thread.currentThread().getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        });

        findViewById(R.id.start_intent_service_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, MyIntentService.class);
                intent.putExtra(MyIntentService.INTENT_KEY_WHAT, MyIntentService.WHAT_MESSAGE);
                startService(intent);
            }
        });
    }
}

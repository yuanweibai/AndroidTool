package rango.tool.androidtool.experiments.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.service.MyIntentService;
import rango.tool.androidtool.service.MyService;
import rango.tool.androidtool.service.TestService;
import rango.tool.androidtool.workmanager.ToolWorkManager;
import rango.tool.common.utils.Worker;

public class ServiceActivity extends BaseActivity {

    private static final String TAG = "ServiceActivity";

    private static class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private static ServiceConnection serviceConnection = new MyServiceConnection();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_layout);


        findViewById(R.id.start_service_btn).setOnClickListener(v -> {

            Log.e("TestService", "start service....");
            Intent intent = new Intent(ServiceActivity.this, TestService.class);
            startService(intent);

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
                @Override
                public void run() {
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
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, MyIntentService.class);
                intent.putExtra(MyIntentService.INTENT_KEY_WHAT, MyIntentService.WHAT_MESSAGE);
                startService(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = ToolApplication.getRefWatcher();
        watcher.watch(this);
        Log.e(TAG, "onDestroy()");
    }
}

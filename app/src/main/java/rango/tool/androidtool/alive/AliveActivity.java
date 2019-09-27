package rango.tool.androidtool.alive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.alive.process.AliveService;
import rango.tool.androidtool.alive.process.LocalService;
import rango.tool.androidtool.base.BaseActivity;

public class AliveActivity extends BaseActivity {

    private static final String TAG = "AliveActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alive_layout);

        findViewById(R.id.task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(AliveActivity.this, DownloadService.class));
            }
        });

        findViewById(R.id.double_process_alive_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startService(new Intent(AliveActivity.this, LocalService.class));
                startService(new Intent(AliveActivity.this, AliveService.class));
            }
        });

        findViewById(R.id.one_pixel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AliveManager.getInstance().setStartOnePixelAlive();
            }
        });

        findViewById(R.id.music_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AliveManager.getInstance().startMusicService();
            }
        });

        findViewById(R.id.front_service_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AliveManager.getInstance().startFrontDeskService();
            }
        });
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

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }
}

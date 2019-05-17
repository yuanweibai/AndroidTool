package rango.tool.androidtool.experiments.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import rango.tool.androidtool.IMyAidl;
import rango.tool.androidtool.R;
import rango.tool.androidtool.aidi.AidlService;
import rango.tool.androidtool.base.BaseActivity;

public class AidlActivity extends BaseActivity {

    private static final String TAG = AidlActivity.class.getSimpleName();

    private IMyAidl myAidl;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: name = " + name);
            myAidl = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: name = " + name);
            myAidl = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aild_layout);

        findViewById(R.id.bind_btn).setOnClickListener(v -> {
            Intent intent = new Intent(AidlActivity.this, AidlService.class);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        });

        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    Log.e(TAG, "processId: " + android.os.Process.myPid());
                    myAidl.sendMsg("main process msg");
                } catch (Exception e) {

                }

            }
        });

        findViewById(R.id.get_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    Log.e(TAG, "processId: " + android.os.Process.myPid() + ", msg: " + myAidl.getMsg());

                } catch (Exception e) {

                }

            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}

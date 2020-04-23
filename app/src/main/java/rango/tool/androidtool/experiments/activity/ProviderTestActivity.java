package rango.tool.androidtool.experiments.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.provider.OtherProcessProvider;
import rango.tool.common.utils.Worker;

public class ProviderTestActivity extends BaseActivity {

    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_test);

        Log.e("OtherProcessProvider", "activity: thread = " + Thread.currentThread().getName());

        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker.postWorker(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10000; i++) {
                            Bundle bundle = getContentResolver().call(OtherProcessProvider.createRemoteConfigContentUri(), "rango_r", null, null);
                            if (bundle != null) {
                                int value = bundle.getInt("rango_int");
                                Log.d("rango-1", "value = " + value);
                            }
                        }
                    }
                });

                Worker.postWorker(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10000; i++) {
                            Bundle bundle = getContentResolver().call(OtherProcessProvider.createRemoteConfigContentUri(), "rango_r", null, null);
                            if (bundle != null) {
                                int value = bundle.getInt("rango_int");
                                Log.d("rango-2", "value = " + value);
                            }
                        }
                    }
                });

            }
        });

        findViewById(R.id.write_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker.postWorker(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 1000; i++) {
                            Bundle bundle = getContentResolver().call(OtherProcessProvider.createRemoteConfigContentUri(), "rango_r", null, null);
                            if (bundle != null) {
                                int value = bundle.getInt("rango_int");
                                Log.d("rango-2", "value = " + value);
                            }
                        }
                    }
                });

            }
        });

        findViewById(R.id.query_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(OtherProcessProvider.createRemoteConfigContentUri(), null, null, null, null);
                if (cursor != null) {
                    cursor.close();
                }
            }
        });

        findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                getContentResolver().insert(OtherProcessProvider.createRemoteConfigContentUri(), contentValues);
            }
        });
        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(OtherProcessProvider.createRemoteConfigContentUri(), null, null);
            }
        });
        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker.postWorker(new Runnable() {
                    @Override
                    public void run() {
                        getContentResolver().update(OtherProcessProvider.createRemoteConfigContentUri(), null, null, null);
                    }
                });

            }
        });
    }
}

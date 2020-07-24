package rango.tool.androidtool.experiments.activity;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.dialog.CustomDialog;
import rango.tool.common.utils.ScreenUtils;

public class AnyThingActivity extends BaseActivity {

    private Thread mThread;

    private TextView valueText;

    private class CalledFromWrongThreadException extends RuntimeException {

        public CalledFromWrongThreadException(String dd) {
        }
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(ToolContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ScreenUtils.setCustomDensity(this, getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_thing_layout);
        valueText = findViewById(R.id.value_text);

        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("rango------","thread = "+Thread.currentThread().getName());
                        Looper.prepare();
                        Dialog dialog = new CustomDialog(AnyThingActivity.this);
                        dialog.show();
                        Looper.loop();
                    }
                }).start();
            }
        });

    }

}

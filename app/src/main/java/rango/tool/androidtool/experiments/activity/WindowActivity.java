package rango.tool.androidtool.experiments.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class WindowActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_layout);

        findViewById(R.id.activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                showWindow(wm);
            }
        });

        findViewById(R.id.application_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                showWindow(wm);
            }
        });
    }

    private void showWindow(WindowManager wm) {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.token = getWindow().getDecorView().getWindowToken();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 500;
        params.gravity = Gravity.CENTER;
        final View view = LayoutInflater.from(this).inflate(R.layout.window_alert_layout, null);
        wm.addView(view, params);
    }


}

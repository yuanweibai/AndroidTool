package rango.tool.androidtool.experiments.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.base.BaseActivity;

public class WindowActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_layout);

        findViewById(R.id.activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow();
            }
        });

        findViewById(R.id.application_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow();
            }
        });
    }

    public static void showWindow() {
        WindowManager wm = (WindowManager) ToolApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        final View view = LayoutInflater.from(ToolApplication.getContext()).inflate(R.layout.window_alert_layout, null);
        view.findViewById(R.id.remove_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(view);
            }
        });
        wm.addView(view, params);
    }


}

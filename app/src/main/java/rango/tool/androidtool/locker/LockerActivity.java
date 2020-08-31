package rango.tool.androidtool.locker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.Window;
import android.view.WindowManager;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.base.BaseActivity;

public class LockerActivity extends BaseActivity {

    public static void start() {
        Context context = ToolApplication.getContext();
        Intent starter = new Intent(context, LockerActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_locker_layout);
        findViewById(R.id.close_btn).setOnClickListener(v -> finish());
    }

}

package rango.tool.androidtool.workmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class WorkManagerActivity extends BaseActivity {

    private TextView logText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);

        findViewById(R.id.work_manager_delay).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testDelay();
            Toast.makeText(WorkManagerActivity.this, "worker delay_1 successfully!!!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.work_manager_delay_2).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testDelay2();
            Toast.makeText(WorkManagerActivity.this, "worker delay_2 successfully!!!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.work_manager_delay_3).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testDelay3();
            Toast.makeText(WorkManagerActivity.this, "worker delay_3 successfully!!!", Toast.LENGTH_SHORT).show();
        });


        findViewById(R.id.work_manager_periodic).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testPeriodic();
            Toast.makeText(WorkManagerActivity.this, "worker periodic successfully!!!", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.work_manager_periodic_no_first).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testPeriodic2();
            Toast.makeText(WorkManagerActivity.this, "worker periodic 2 successfully!!!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.work_manager_periodic_3).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testPeriodic3();
            Toast.makeText(WorkManagerActivity.this, "worker periodic 3 successfully!!!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.cancel_all_work_btn).setOnClickListener(v -> {
            ToolWorkManager.getInstance().cancelAllWork();
            Toast.makeText(WorkManagerActivity.this, "cancel all work successfully!!!", Toast.LENGTH_SHORT).show();
        });

        logText = findViewById(R.id.log_text);
        logText.setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.read_log_btn).setOnClickListener(v -> {
            StringBuilder stringBuilder = ToolWorkManager.getInstance().readMsg();
            if (stringBuilder == null || TextUtils.isEmpty(stringBuilder.toString())) {
                logText.setText("null");
            } else {
                logText.setText(stringBuilder.toString());
            }
        });

        findViewById(R.id.write_log_btn).setOnClickListener(v -> ToolWorkManager.getInstance().writeMsg("test test test test test test test test!!!"));

        findViewById(R.id.clear_log_btn).setOnClickListener(v -> ToolWorkManager.getInstance().clearMsg());
    }
}

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
            Toast.makeText(WorkManagerActivity.this, "worker delay successfully!!!", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.work_manager_periodic).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testPeriodic();
            Toast.makeText(WorkManagerActivity.this, "worker periodic successfully!!!", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.work_manager_periodic_no_first).setOnClickListener(v -> {
            ToolWorkManager.getInstance().testPeriodicNoFirst();
            Toast.makeText(WorkManagerActivity.this, "worker periodic no first successfully!!!", Toast.LENGTH_SHORT).show();
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

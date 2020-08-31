package rango.tool.androidtool.job;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class JobActivity extends BaseActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        textView = findViewById(R.id.log_text);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.delay).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ToolEventJob.delayToolJob();
            }
        });
        findViewById(R.id.delay_2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ToolEventJob.delayToolJob2();
            }
        });
        findViewById(R.id.delay_3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                TestJob.delayToolJob3();
            }
        });

        findViewById(R.id.periodic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ToolEventJob.periodicToolJob();
            }
        });

        findViewById(R.id.periodic_2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ToolEventJob.periodicToolJob2();
            }
        });
        findViewById(R.id.periodic_3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                TestJob.periodicToolJob3();
            }
        });

        findViewById(R.id.read_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                StringBuilder stringBuilder = MainJobCreator.readMsg();
                if (stringBuilder == null || TextUtils.isEmpty(stringBuilder.toString())) {
                    textView.setText("null");
                } else {
                    textView.setText(stringBuilder.toString());
                }

            }
        });

        findViewById(R.id.clear_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                MainJobCreator.clearMsg();
                textView.setText("null");
            }
        });

        findViewById(R.id.write_log_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String msg = "log log log log log log";
                MainJobCreator.writeMsg(msg);
            }
        });

        findViewById(R.id.cancel_all_work_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            }
        });
    }
}

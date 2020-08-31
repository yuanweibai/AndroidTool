package rango.tool.androidtool.thread;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class ThreadMemoryActivity extends BaseActivity {

    private TextView contentText;

    private long totalMemory;
    private long lastMemory;
    private long currentMemory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_memory);

        contentText = findViewById(R.id.content_text);
        findViewById(R.id.start_thread_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastMemory = currentMemory;
                new Thread(new Runnable() {
                    @Override public void run() {
                        while (true) {
                            try {
                                Log.e("rango", "thread running......");
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {

                            }
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.start_HandlerThread_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                lastMemory = currentMemory;
            }
        });

        findViewById(R.id.result_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentText.setText(analyzeMemory());
            }
        });
    }

    private String analyzeMemory() {
        Runtime mRuntime = Runtime.getRuntime();
        totalMemory = mRuntime.totalMemory();
        currentMemory = totalMemory - mRuntime.freeMemory();
        String result = "线程数量：" + Thread.activeCount() + ", TotalMemory: " + totalMemory + " bytes\n"
                + "UsedMemory:" + currentMemory + " bytes\n"
                + "increase: " + (currentMemory - lastMemory) + " bytes\n"
                + "---------------------------------\n";
        return result;
    }
}

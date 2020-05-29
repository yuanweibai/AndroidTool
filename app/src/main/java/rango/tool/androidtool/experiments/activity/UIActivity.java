package rango.tool.androidtool.experiments.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class UIActivity extends BaseActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_layout);

        textView = findViewById(R.id.text_btn);


        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("真牛逼");
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        test();
    }

    private void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                textView.setText("真牛逼-----------");
            }
        }).start();
    }
}

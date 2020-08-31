package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.view.CountDownTextView;

/**
 * Created by baiyuanwei on 17/11/20.
 */

public class CanvasActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_layout);

        CountDownTextView textView = findViewById(R.id.count_down_text);
        textView.setOnClickListener(v -> textView.startCountDown(10));
    }
}

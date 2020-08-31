package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.Worker;

public class StackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_layout);

        findViewById(R.id.stack_btn).setOnClickListener(v -> startAnim());
    }

    private void startAnim() {
        Log.e("rango", "stack-----------------------------------over!!!!");
        Worker.postMain(new Runnable() {
            @Override public void run() {
                startAnim();
            }
        }, 10);
    }
}

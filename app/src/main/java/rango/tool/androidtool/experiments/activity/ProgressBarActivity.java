package rango.tool.androidtool.experiments.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class ProgressBarActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar_layout);

        ProgressBar bar = findViewById(R.id.progress_bar);

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            bar.setProgress(value);
        });
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());

        findViewById(R.id.start_btn).setOnClickListener(v -> animator.start());
    }
}

package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class RippleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple_layout);

        findViewById(R.id.text_1_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });

        findViewById(R.id.text_2_btn).setClickable(true);
    }
}

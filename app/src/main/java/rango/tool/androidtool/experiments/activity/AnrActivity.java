package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class AnrActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr_layout);

        findViewById(R.id.start_btn).setOnClickListener(v -> {
            while (true) {

            }
        });

        findViewById(R.id.start_2_btn).setOnClickListener(v -> Toast.makeText(AnrActivity.this, "hhhhhhhh", Toast.LENGTH_LONG).show());
    }
}

package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.view.CircleEffectTextView;
import rango.tool.androidtool.view.WaveButton;

public class ButtonActivity extends BaseActivity {
    private WaveButton waveButton;
    private CircleEffectTextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_layout);
        waveButton = findViewById(R.id.wave_btn);
        textView = findViewById(R.id.circle_effect_btn);
        textView.setEffectView(findViewById(R.id.circle_effect_view));

        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveButton.startWave();
            }
        });

    }
}

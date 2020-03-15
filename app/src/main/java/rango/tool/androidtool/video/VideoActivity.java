package rango.tool.androidtool.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.video.audio.AudioActivity;
import rango.tool.androidtool.video.audio.MediaRecorderActivity;

public class VideoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        findViewById(R.id.audio_btn).setOnClickListener(v -> startActivity(AudioActivity.class));
        findViewById(R.id.media_recorder_btn).setOnClickListener(v -> startActivity(MediaRecorderActivity.class));
    }
}
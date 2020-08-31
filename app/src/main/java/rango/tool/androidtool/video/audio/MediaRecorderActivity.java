package rango.tool.androidtool.video.audio;

import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.File;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class MediaRecorderActivity extends BaseActivity {

    private static final String AUDIO_FILE_PATH = Environment.getExternalStorageDirectory() + "/VideoTest/MediaRecorder/";
    private static final String AUDIO_NAME = "media.arm";

    private MediaRecorder mediaRecorder;
    private SoundPool soundPool;
    private int streamId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);


        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });

        findViewById(R.id.play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAndPlayAudio();
            }
        });

        findViewById(R.id.stop_play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
            }
        });
    }

    private void initSoundPool() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.e("rango", "sampleId = " + sampleId);
                streamId = soundPool.play(sampleId, 1f, 1f, 0, -1, 1.0f);
            }
        });
    }

    private void loadAndPlayAudio() {
        if (soundPool == null) {
            initSoundPool();
        }

        File file = new File(AUDIO_FILE_PATH + AUDIO_NAME);

        int soundId = soundPool.load(file.getAbsolutePath(), 1);
        Log.e("rango", "soundId = " + soundId);
    }

    private void startRecord() {
        if (mediaRecorder == null) {
            mediaRecorder = initMediaRecorder();
        }

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        if (mediaRecorder == null) {
            return;
        }

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private MediaRecorder initMediaRecorder() {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(1024 * 1024);

        File file = new File(AUDIO_FILE_PATH + AUDIO_NAME);

        if (file.exists()) {
            file.delete();
        }

        File dir = new File(AUDIO_FILE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        recorder.setOutputFile(file.getAbsolutePath());

        return recorder;
    }

    private void stopPlay() {
        if (soundPool == null) {
            return;
        }

        soundPool.stop(streamId);
        soundPool.release();
        soundPool = null;
    }


}

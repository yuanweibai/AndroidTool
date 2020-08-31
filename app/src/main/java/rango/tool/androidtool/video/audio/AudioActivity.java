package rango.tool.androidtool.video.audio;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class AudioActivity extends BaseActivity {

    private String audioFilePath = Environment.getExternalStorageDirectory().getPath() + "/VideoTest/audio/";
    private String audioFIleName = "test.pcm";

    private AudioRecord audioRecord;

    private volatile boolean isRecording;

    int audioSource = MediaRecorder.AudioSource.MIC;
    int sampleRateInHz = 16000;
    int audioChannel = AudioFormat.CHANNEL_IN_MONO;
    int pcmFormat = AudioFormat.ENCODING_PCM_16BIT;

    private int bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, audioChannel, pcmFormat);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_layout);

        findViewById(R.id.get_permission_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPermissions(AudioActivity.this);
            }
        });

        findViewById(R.id.start_record_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecordAudio();
            }
        });

        findViewById(R.id.stop_record_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecordAudio();
            }
        });

        findViewById(R.id.play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });
    }

    private void startRecordAudio() {
        if (audioRecord == null) {
            audioRecord = initAudioRecord();
        }

        final byte data[] = new byte[bufferSize];
        File audioDir = new File(audioFilePath);
        final File file = new File(audioFilePath + audioFIleName);
        if (file.exists()) {
            file.delete();
        }

        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }

        audioRecord.startRecording();
        isRecording = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (outputStream != null) {
                    while (isRecording) {
                        int read = audioRecord.read(data, 0, bufferSize);
                        if (read != AudioRecord.ERROR_INVALID_OPERATION) {
                            try {
                                outputStream.write(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private AudioRecord initAudioRecord() {

        return new AudioRecord(audioSource,
                sampleRateInHz,
                audioChannel,
                pcmFormat,
                bufferSize);
    }

    private void stopRecordAudio() {
        isRecording = false;
        if (audioRecord == null) {
            return;
        }

        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
    }

    private void playAudio() {
        final String path = audioFilePath + audioFIleName;
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRateInHz,
                AudioFormat.CHANNEL_OUT_MONO,
                pcmFormat,
                bufferSize,
                AudioTrack.MODE_STREAM);


        audioTrack.play();

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in = null;
                try {
                    in = new FileInputStream(path);
                    byte[] tempBuffer = new byte[bufferSize];
                    int readCount = 0;
                    while (in.available() > 0) {
                        readCount = in.read(tempBuffer);
                        if (readCount > 0) {
                            audioTrack.write(tempBuffer, 0, readCount);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private static final int REQUEST_CODE = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

    private static void verifyPermissions(Activity activity) {
        try {
            if (!checkPermissions(activity)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE);
            }
        } catch (Exception ignore) {
        }
    }

    private static boolean checkPermissions(Activity activity) {
        int readStorageStatus = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeStorageStatus = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordAudioStatus = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);

        return readStorageStatus == PackageManager.PERMISSION_GRANTED
                && writeStorageStatus == PackageManager.PERMISSION_GRANTED
                && recordAudioStatus == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            Log.e("rango", "request permission result: " + checkPermissions(AudioActivity.this));
        }
    }
}

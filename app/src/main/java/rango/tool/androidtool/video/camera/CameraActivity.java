package rango.tool.androidtool.video.camera;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class CameraActivity extends BaseActivity {

    private static final String TAG = "CameraActivity";

    private HandlerThread cameraThread;
    private Handler cameraHandler;

    private TextureView cameraTexture;
    private File pictureFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_layout);

        cameraTexture = findViewById(R.id.camera_texture);
        findViewById(R.id.take_picture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        pictureFile = new File(getExternalFilesDir(null), "picture.jpg");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraThread();

        if (cameraTexture.isAvailable()) {
            openCamera(cameraTexture.getWidth(), cameraTexture.getHeight());
        } else {
            cameraTexture.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    private void openCamera(int width, int height) {

    }

    private void startCameraThread() {
        cameraThread = new HandlerThread("CameraThread");
        cameraThread.start();
        cameraHandler = new Handler(cameraThread.getLooper());
    }

    private void takePicture() {

    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.e(TAG, "onSurfaceTextureAvailable(): width = " + width + ", height = " + height);
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.e(TAG, "onSurfaceTextureSizeChanged()");

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.e(TAG, "onSurfaceTextureDestroyed()");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            Log.e(TAG, "onSurfaceTextureUpdated()");

        }
    };
}

package rango.tool.androidtool.experiments.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rango.tool.androidtool.GlideApp;
import rango.tool.androidtool.GlideRequest;
import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.FileUtils;

public class GifImageActivity extends BaseActivity {

    private static final String TAG = "GifImageActivity";

    private static final String CACHE_DIR_NAME = "GifCache";

    private static final String GIF_URL = "https://media.giphy.com/media/f4Uvqh6zELReymrel9/giphy.gif";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_layout);

        ImageView imageView = findViewById(R.id.image_view);
        final long a = System.currentTimeMillis();
        RequestBuilder<File> requestBuilder = GlideApp.with(this).downloadOnly();
        requestBuilder.load(GIF_URL);
        ((GlideRequest<File>) requestBuilder).placeholder(R.drawable.hhh_test);
        requestBuilder.listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                Log.e(TAG, "onLoadFailed: " + e);
                return true;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                Log.e(TAG, "onResourceReady: " + (System.currentTimeMillis() - a));
                String filePath = getFilePath(GIF_URL);
                File file = new File(filePath);
                if (file.exists() && file.length() > 0) {
                    loadFile(file, imageView);
                } else {
                    long b = System.currentTimeMillis();
                    if (FileUtils.copyFile(resource.getAbsolutePath(), filePath)) {
                        Log.e(TAG, "copyFile: " + (System.currentTimeMillis() - b));
                        if (file.exists() && file.length() > 0) {
                            loadFile(file, imageView);
                        }
                    }
                }
                return true;
            }
        });
        requestBuilder.preload();

        Log.e(TAG, "hhhhhhh = " + (System.currentTimeMillis() - a));
        ImageView imageView2 = findViewById(R.id.image_view_2);
        GlideApp.with(this)
                .load(GIF_URL)
                .into(imageView2);

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Drawable drawable = imageView2.getDrawable();
                if (drawable instanceof GifDrawable) {
                    ((GifDrawable) drawable).stop();
                }
            }
        });

        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Drawable drawable = imageView2.getDrawable();
                if (drawable instanceof GifDrawable) {
                    ((GifDrawable) drawable).start();
                }
            }
        });

        Log.e(TAG, "cccc = " + (System.currentTimeMillis() - a));
    }

    private void loadFile(File file, ImageView imageView) {
        GlideApp.with(GifImageActivity.this)
                .load(file)
                .into(imageView);
    }

    private void copyFile(File sourceFile, String targetFilePath) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(targetFilePath);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }

                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFilePath(String url) {
        return FileUtils.getCacheDirectory(this, CACHE_DIR_NAME).getAbsolutePath() + FileUtils.md5(url);
    }
}

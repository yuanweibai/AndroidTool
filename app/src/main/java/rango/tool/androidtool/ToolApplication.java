package rango.tool.androidtool;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.os.TraceCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.evernote.android.job.JobManager;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.InputStream;
import java.nio.ByteBuffer;

import rango.tool.androidtool.base.BaseApplication;
import rango.tool.androidtool.job.MainJobCreator;
import rango.tool.androidtool.webp.libwebp.WebpBytebufferDecoder;
import rango.tool.androidtool.webp.libwebp.WebpResourceDecoder;
import rango.tool.common.utils.Worker;

public class ToolApplication extends BaseApplication {

    private static final String TAG = ToolApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");

        TraceCompat.beginSection("Rango");

        JobManager.create(this).addJobCreator(new MainJobCreator());
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT < 24) {
//            JobConfig.setAllowSmallerIntervalsForMarshmallow(true);
        }

        Fresco.initialize(this);

        TraceCompat.beginSection("Rango#test");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TraceCompat.endSection();
        }

        ResourceDecoder decoder = new WebpResourceDecoder(this);
        ResourceDecoder byteDecoder = new WebpBytebufferDecoder(this);
        // use prepend() avoid intercept by default decoder
        Glide.get(this).getRegistry()
                .prepend(InputStream.class, Drawable.class, decoder)
                .prepend(ByteBuffer.class, Drawable.class, byteDecoder);


        TraceCompat.endSection();
    }

    @Override
    public void onTerminate() {
        Worker.destroy();
        super.onTerminate();
    }
}

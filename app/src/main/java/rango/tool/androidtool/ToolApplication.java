package rango.tool.androidtool;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Process;
import android.support.v4.os.TraceCompat;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.evernote.android.job.JobManager;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import rango.tool.androidtool.base.BaseApplication;
import rango.tool.androidtool.job.MainJobCreator;
import rango.tool.androidtool.webp.libwebp.WebpBytebufferDecoder;
import rango.tool.androidtool.webp.libwebp.WebpResourceDecoder;
import rango.tool.common.utils.CommonManager;
import rango.tool.common.utils.Worker;

public class ToolApplication extends BaseApplication {

    private static final String TAG = ToolApplication.class.getSimpleName();

    private String processName;

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
//            Thread.sleep(3000);
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

        CommonManager.getInstance().setCommonCallable(ToolApplication::getContext);

        TraceCompat.endSection();
    }

    private String getToolProcessName() {
        if (TextUtils.isEmpty(processName)) {
            processName = searchProcessName();
        }
        return processName;
    }

    private static String searchProcessName() {
        String processName = null;

        try {
            File file = new File("/proc/" + Process.myPid() + "/cmdline");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            processName = bufferedReader.readLine().trim();
            bufferedReader.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        if (TextUtils.isEmpty(processName)) {
            ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager == null) {
                return processName;
            }
            List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
            if (processInfos != null) {
                int pid = Process.myPid();

                for (ActivityManager.RunningAppProcessInfo appProcess : processInfos) {
                    if (appProcess.pid == pid) {
                        processName = appProcess.processName;
                        break;
                    }
                }
            }
        }

        return processName;
    }

    @Override
    public void onTerminate() {
        Worker.destroy();
        super.onTerminate();
    }
}

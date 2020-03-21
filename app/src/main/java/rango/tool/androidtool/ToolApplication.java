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
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xsj.crasheye.Crasheye;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import rango.tool.androidtool.base.BaseApplication;
import rango.tool.androidtool.job.MainJobCreator;
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

        Crasheye.init(this, "4b2829a0");

        UMConfigure.init(this, "5da1c4084ca357ebdd000c38", "hhhhh", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        CrashReport.initCrashReport(this, "586293be04", true);

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

        CommonManager.getInstance().setCommonCallable(ToolApplication::getContext);

        initLeakCanary();

        TraceCompat.endSection();
    }

    private RefWatcher refWatcher;

    private void initLeakCanary() {
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher() {
        return ((ToolApplication) getContext().getApplicationContext()).refWatcher;
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

package rango.tool.androidtool;

import android.os.Build;
import android.util.Log;

import com.evernote.android.job.JobConfig;
import com.evernote.android.job.JobManager;

import rango.tool.androidtool.base.BaseApplication;
import rango.tool.androidtool.job.MainJobCreator;
import rango.tool.androidtool.job.ToolEventJob;
import rango.tool.androidtool.workmanager.ToolWorkManager;
import rango.tool.common.utils.Worker;

public class ToolApplication extends BaseApplication {

    private static final String TAG = ToolApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");

        JobManager.create(this).addJobCreator(new MainJobCreator());
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT < 24) {
            JobConfig.setAllowSmallerIntervalsForMarshmallow(true);
        }

        ToolEventJob.initJobs();
        ToolWorkManager.init();
    }

    @Override
    public void onTerminate() {
        Worker.destroy();
        super.onTerminate();
    }
}

package rango.tool.androidtool.job;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.os.TraceCompat;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import rango.tool.common.utils.TimeUtils;

public class TestJob extends Job {

    public static final String TAG = "TestJob";

    private static final String JOB_TYPE = "job_type";
    private static final String JOB_MSG = "job_msg";

    private static final int DELAY = 1;
    private static final int PERIODIC = 2;

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Bundle bundle = params.getTransientExtras();
        int jobType = bundle.getInt(JOB_TYPE, -1);
        switch (jobType) {
            case PERIODIC:
                String msg = "run: periodic_3,time = " + TimeUtils.getCurrentTime();
                MainJobCreator.writeMsg(msg);
                break;
            case DELAY:
                String delayMsg = "run: delay_3, time = " + TimeUtils.getCurrentTime();
                MainJobCreator.writeMsg(delayMsg);
                break;
            default:
                break;
        }
        return Result.SUCCESS;
    }

    public static void periodicToolJob3() {
        Log.d(TAG, "------periodicToolJob------");
        String msg = "start: periodic_3, duration = 4h, time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_TYPE, PERIODIC);
        bundle.putString(JOB_MSG, "periodic_3 too job msg");

        long periodicMills = 4 * 60 * 60 * 1000;
        periodicJob(bundle, periodicMills);
    }

    public static void delayToolJob3() {
        Log.d(TAG, "------delayToolJob------");
        String msg = "start: delay_3, duration = 3h, time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_TYPE, DELAY);
        bundle.putString(JOB_MSG, "delay_3 tool job msg");
        long delayMills = 3 * 60 * 60 * 1000;
        delayJob(bundle, delayMills);
    }

    private static void periodicJob(Bundle bundle, long periodicMills) {
        Log.d(TAG, "------periodicJob: bundle = " + bundle.toString() + ", periodicMills = " + periodicMills);
        TraceCompat.beginSection(TAG + "#periodicJob");
        try {
            int jobId = new JobRequest.Builder(TAG)
                    .setPeriodic(periodicMills, 5 * 60 * 1000)
                    .setTransientExtras(bundle)
                    .build()
                    .schedule();
        } finally {
            TraceCompat.endSection();
        }
    }

    private static void delayJob(Bundle bundle, long delay) {
        Log.d(TAG, "------delayJob: bundle = " + bundle.toString() + ", delay = " + delay);
        TraceCompat.beginSection(TAG + "#delayJob");
        try {
            new JobRequest.Builder(TAG)
                    .setExact(delay)
                    .setTransientExtras(bundle)
                    .build()
                    .schedule();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            TraceCompat.endSection();
        }
    }
}

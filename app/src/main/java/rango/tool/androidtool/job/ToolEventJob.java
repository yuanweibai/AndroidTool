package rango.tool.androidtool.job;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.os.TraceCompat;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import rango.tool.androidtool.util.SharedPreferenceHelper;
import rango.tool.common.utils.TimeUtils;

public class ToolEventJob extends Job {

    public static final String TAG = "ToolEventJob";

    private static final String KEY_JOB_ID = "job_id";
    private static final String JOB_TYPE = "job_type";
    private static final String JOB_MSG = "job_msg";
    private static final int UPDATE_TOOL_EVENT = 0;
    private static final int UPDATE_TOOL_2_EVENT = 1;
    private static final int UPDATE_TOOL_3_EVENT = 2;
    private static final int UPDATE_TOOL_4_EVENT = 3;

    public static void initJobs() {
        Log.d(TAG, "------initJobs------");
        cancelAllForTag();
//        cancelAllForId();
        periodicToolJob();

        delayToolJob();
    }

    public static void periodicToolJob() {
        Log.d(TAG, "------periodicToolJob------");
        String msg = "start: periodic_1, duration = 1h,time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_TYPE, UPDATE_TOOL_EVENT);
        bundle.putString(JOB_MSG, "periodic too job msg");
        long periodicMills = 60 * 60 * 1000;
        periodicJob(bundle, periodicMills);
    }

    public static void periodicToolJob2() {
        Log.d(TAG, "------periodicToolJob------");
        String msg = "start: periodic_2, duration = 3h, time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_TYPE, UPDATE_TOOL_3_EVENT);
        bundle.putString(JOB_MSG, "periodic_2 too job msg");
        long periodicMills = 3 * 60 * 60 * 1000;
        periodicJob(bundle, periodicMills);
    }

    public static void delayToolJob() {
        Log.d(TAG, "------delayToolJob------");
        String msg = "start: delay_1, duration = 1h,time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_TYPE, UPDATE_TOOL_2_EVENT);
        bundle.putString(JOB_MSG, "delay tool job msg");
        long delayMills = 60 * 60 * 1000;
        delayJob(bundle, delayMills);
    }

    public static void delayToolJob2() {
        Log.d(TAG, "------delayToolJob------");
        String msg = "start: delay_2, duration = 2h,time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_TYPE, UPDATE_TOOL_4_EVENT);
        bundle.putString(JOB_MSG, "delay_2 tool job msg");
        long delayMills = 2 * 60 * 60 * 1000;
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
            SharedPreferenceHelper.getInstance().putInt(KEY_JOB_ID, jobId);
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

    private static void cancelAllForTag() {
        Log.d(TAG, "------ cancelAllForTag --------");
        JobManager.instance().cancelAllForTag(TAG);
    }

    private static void cancelAllForId() {
        Log.d(TAG, "------ cancelAllForId --------");
        int jobId = SharedPreferenceHelper.getInstance().getInt(KEY_JOB_ID, -1);
        Log.d(TAG, "------ cancelAllForId: jobId = " + jobId);
        if (jobId != -1) {
            JobManager.instance().cancel(jobId);
        }

    }

    @NonNull @Override
    protected Job.Result onRunJob(Params params) {

        Bundle bundle = params.getTransientExtras();
        int jobType = bundle.getInt(JOB_TYPE, -1);
        switch (jobType) {
            case UPDATE_TOOL_EVENT:
                updateToolEvent(bundle);
                break;
            case UPDATE_TOOL_2_EVENT:
                updateTool2Event(bundle);
                break;
            case UPDATE_TOOL_3_EVENT:
                updateTool3Event(bundle);
                break;
            case UPDATE_TOOL_4_EVENT:
                updateTool4Event(bundle);
                break;
            default:
                break;
        }
        return Result.SUCCESS;
    }

    private void updateToolEvent(Bundle bundle) {
        Log.d(TAG, "------updateToolEvent: bundle = " + bundle.toString());
        String msg = "run: periodic_1, time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
    }

    private void updateTool2Event(Bundle bundle) {
        Log.d(TAG, "------updateTool2Event: bundle = " + bundle.toString());
        String msg = "run: delay_1, time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
    }

    private void updateTool3Event(Bundle bundle) {
        Log.d(TAG, "------updateTool2Event: bundle = " + bundle.toString());
        String msg = "run: periodic_2, time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
    }

    private void updateTool4Event(Bundle bundle) {
        Log.d(TAG, "------updateTool2Event: bundle = " + bundle.toString());
        String msg = "run: delay_2, time = " + TimeUtils.getCurrentTime();
        MainJobCreator.writeMsg(msg);
    }

}

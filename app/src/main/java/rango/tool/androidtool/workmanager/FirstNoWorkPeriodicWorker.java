package rango.tool.androidtool.workmanager;

import android.util.Log;

import rango.tool.common.utils.TimeUtils;

public class FirstNoWorkPeriodicWorker extends BaseFirstNoWorkPeriodicWorker {
    private static final String TAG = FirstNoWorkPeriodicWorker.class.getSimpleName();

    public static final String WORKER_TAG = "first_no_work_periodic_worker";

    @Override
    public Result doPeriodicWork() {
        Log.e(TAG, "do periodic work, but first not do!!!");
        String msg = "run: type = work_periodic, current_time = " + TimeUtils.getCurrentTime() + ", do periodic work, but first not do!!!;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.SUCCESS;
    }

    @Override
    public String getKey() {
        return "first_no_work_periodic_worker";
    }


}

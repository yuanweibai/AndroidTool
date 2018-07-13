package rango.tool.androidtool.workmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import rango.tool.common.utils.TimeUtils;

public class PeriodicWorker2 extends Worker {
    private static final String TAG = PeriodicWorker2.class.getSimpleName();

    public static final String WORKER_TAG = "periodic_worker_2";

    @NonNull @Override public Result doWork() {
        Log.e(TAG, "do periodic work!!!");
        String msg = "run: type = work_periodic_2, current_time = " + TimeUtils.getCurrentTime() + ", do periodic work;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.SUCCESS;
    }
}

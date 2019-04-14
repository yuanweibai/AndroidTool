package rango.tool.androidtool.workmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import rango.tool.common.utils.TimeUtils;

public class PeriodicWorker2 extends Worker {
    private static final String TAG = PeriodicWorker2.class.getSimpleName();

    public static final String WORKER_TAG = "periodic_worker_2";

    public PeriodicWorker2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull @Override public Result doWork() {
        Log.e(TAG, "do periodic work!!!");
        String msg = "run: type = work_periodic_2, current_time = " + TimeUtils.getCurrentTime() + ", do periodic work;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.success();
    }
}

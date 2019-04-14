package rango.tool.androidtool.workmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import rango.tool.common.utils.TimeUtils;

public class PeriodicWorker3 extends Worker {
    private static final String TAG = PeriodicWorker3.class.getSimpleName();

    public static final String WORKER_TAG = "periodic_worker_3";

    public PeriodicWorker3(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull @Override public Result doWork() {
        Log.e(TAG, "do periodic work!!!");
        String msg = "run: type = work_periodic_3, current_time = " + TimeUtils.getCurrentTime() + ", do periodic work;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.success();
    }
}

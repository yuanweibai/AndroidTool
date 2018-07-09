package rango.tool.androidtool.workmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import rango.tool.common.utils.TimeUtils;

public class PeriodicWorker extends Worker {

    private static final String TAG = PeriodicWorker.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "do periodic work!!!");
        String msg = "run: type = work_periodic, current_time = " + TimeUtils.getCurrentTime() + ", do periodic work;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.SUCCESS;
    }
}

package rango.tool.androidtool.workmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import rango.tool.common.utils.TimeUtils;

public class PeriodicWorker extends BaseWorker {

    private static final String TAG = PeriodicWorker.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "do periodic work!!!");
        String msg = "run: type = work_periodic, current_time = " + TimeUtils.getCurrentTime() + ", do periodic work;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.SUCCESS;
    }

    @Override
    public String getTag() {
        return "PeriodicWorker";
    }
}

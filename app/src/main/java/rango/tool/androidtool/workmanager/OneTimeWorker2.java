package rango.tool.androidtool.workmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import rango.tool.common.utils.TimeUtils;

public class OneTimeWorker2 extends Worker {
    public static final String TAG = OneTimeWorker2.class.getSimpleName();

    public OneTimeWorker2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "do one time work!!!");
        String msg = "run: type = work_delay_2, current_time = " + TimeUtils.getCurrentTime() + ", do delay work;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.success();
    }
}

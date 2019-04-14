package rango.tool.androidtool.workmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import rango.tool.common.utils.TimeUtils;

public class OneTimeWorker3 extends Worker {
    private static final String TAG = OneTimeWorker3.class.getSimpleName();

    public OneTimeWorker3(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "do one time work!!!");
        String msg = "run: type = work_delay_3, current_time = " + TimeUtils.getCurrentTime() + ", do delay work;\n";
        ToolWorkManager.getInstance().writeMsg(msg);
        return Result.success();
    }
}

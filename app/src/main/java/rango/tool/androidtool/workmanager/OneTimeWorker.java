package rango.tool.androidtool.workmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import rango.tool.androidtool.service.MyService;
import rango.tool.common.utils.TimeUtils;

public class OneTimeWorker extends Worker {
    private static final String TAG = OneTimeWorker.class.getSimpleName();

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "do one time work_delay_1 work!!! ");
        String msg = "run: type = work_delay_1, current_time = " + TimeUtils.getCurrentTime() + ", do delay work;\n";
//        ToolWorkManager.getInstance().writeMsg(msg);
//        Intent intent = new Intent(ToolApplication.getContext(), TestService.class);
//        ToolApplication.getContext().startService(intent);

        MyService.bindService();

        return Result.SUCCESS;
    }
}

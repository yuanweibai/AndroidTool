package rango.tool.androidtool.workmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import rango.tool.androidtool.util.SharedPreferenceHelper;

public abstract class BaseFirstNoWorkPeriodicWorker extends Worker {

    private static final String TAG = BaseFirstNoWorkPeriodicWorker.class.getSimpleName();

    public static final String PREFERENCE_KEY_FIRST_NO_WORK_PERIODIC_WORKER = "preference_key_first_no_work_periodic_worker_";

    @NonNull
    @Override
    public Result doWork() {
        String key = PREFERENCE_KEY_FIRST_NO_WORK_PERIODIC_WORKER + getKey();
        if (!SharedPreferenceHelper.getInstance().contains(key)) {
            Log.e(TAG, "not do my work!!! because of this is first time running.");
            SharedPreferenceHelper.getInstance().putInt(key, 1);
            return Result.SUCCESS;
        }
        return doPeriodicWork();
    }

    public abstract Result doPeriodicWork();

    public abstract String getKey();
}

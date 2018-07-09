package rango.tool.androidtool.workmanager;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import androidx.work.WorkManager;
import androidx.work.Worker;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.util.SharedPreferenceHelper;

public class MyWorker extends Worker {
    private static final String TAG = MyWorker.class.getSimpleName();

    public static final String PREFERENCE_KEY_MOST_SIG_BITS = "preference_key_uuid_most_sig_bits";
    public static final String PREFERENCE_KEY_LEAST_SIG_BITS = "preference_key_uuid_least_sig_bits";
    public static final String PREFERENCE_KEY_RUNNING_TIMES = "preference_key_running_times";

    @NonNull
    @Override
    public Result doWork() {
        if (!SharedPreferenceHelper.getInstance().contains(PREFERENCE_KEY_RUNNING_TIMES)) {
            Log.e(TAG, "not do my work!!! because of this is first time running.");
            SharedPreferenceHelper.getInstance().putInt(PREFERENCE_KEY_RUNNING_TIMES, 1);
            return Result.SUCCESS;
        }
        Log.e(TAG, "do my work!!!");
        rango.tool.common.utils.Worker.postMain(new Runnable() {
            @Override public void run() {
                Toast.makeText(ToolApplication.getContext(), "do my work", Toast.LENGTH_LONG).show();
            }
        });
        long mostSigBits = SharedPreferenceHelper.getInstance().getLong(PREFERENCE_KEY_MOST_SIG_BITS, 0l);
        long leastSigBits = SharedPreferenceHelper.getInstance().getLong(PREFERENCE_KEY_LEAST_SIG_BITS, 0l);
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        WorkManager.getInstance().cancelWorkById(uuid);
        return Result.SUCCESS;
    }
}

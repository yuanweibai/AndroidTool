package rango.tool.androidtool.service;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;

public class MyService extends BindIntentService {

    private static final String TAG = MyService.class.getSimpleName();

    public MyService() {
        super("MyService");
    }

    public static void bindService() {
        Context context = getContext();
        Intent intent = new Intent(context, MyService.class);
        context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "running......" + ",ThreadId = " + Thread.currentThread().getId());
    }
}

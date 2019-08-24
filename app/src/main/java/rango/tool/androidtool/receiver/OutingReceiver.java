package rango.tool.androidtool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OutingReceiver extends BroadcastReceiver {

    private static final String TAG = OutingReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: " + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
    }
}

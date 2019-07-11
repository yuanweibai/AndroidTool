package rango.tool.androidtool.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import rango.tool.androidtool.experiments.activity.ChargingShowActivity;

public class BatteryBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = BatteryBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            case Intent.ACTION_BATTERY_CHANGED:
                batteryChanged(context);
                break;
            case Intent.ACTION_BATTERY_LOW:
                batteryLow(context);
                break;
            case Intent.ACTION_BATTERY_OKAY:
                batteryOkay(context);
                break;
            case Intent.ACTION_POWER_CONNECTED:
                powerConnected(context);
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                powerDisconnected(context);
                break;
            default:
                break;
        }
    }

    private void batteryChanged(Context context) {
        Log.e(TAG, "batteryChanged");
    }

    private void batteryLow(Context context) {
        Log.e(TAG, "batteryLow");

    }

    private void batteryOkay(Context context) {
        Log.e(TAG, "batteryOkay");
    }

    private void powerConnected(Context context) {
        Log.e(TAG, "powerConnected");
        Intent intent = new Intent(context, ChargingShowActivity.class);
        context.startActivity(intent);
    }

    private void powerDisconnected(Context context) {
        Log.e(TAG, "powerDisconnected");
    }

    public static BatteryBroadcastReceiver register(Context context) {
        BatteryBroadcastReceiver receiver = new BatteryBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        context.registerReceiver(receiver, intentFilter);
        return receiver;
    }

    public static void unRegister(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }
}

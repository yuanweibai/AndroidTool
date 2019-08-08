package rango.tool.androidtool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OtherProcessReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int value = intent.getIntExtra("rango_value", 0);
        Log.e("rango", "process: " + android.os.Process.myPid() + ",value = " + value);

    }
}

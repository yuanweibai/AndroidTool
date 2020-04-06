package rango.tool.androidtool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = TestBroadcastReceiver.class.getSimpleName();

    {
        Log.e("rango", "1111111");
    }

    public TestBroadcastReceiver() {
        super();

        Log.e("rango", "222222");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive......");
    }
}

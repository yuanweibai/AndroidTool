package rango.tool.androidtool.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;

public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    public static final int WHAT_MESSAGE = 1;
    public static final String INTENT_KEY_WHAT = "message_what";

    private int mStartId;

    public MyIntentService() {
        super("RangoIntent");
    }


    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.e(TAG, "onStart: startId = " + startId);
        mStartId = startId;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int what = intent.getIntExtra(INTENT_KEY_WHAT, 0);
        switch (what) {
            case WHAT_MESSAGE:
                Log.e(TAG, "onHandleIntent: what = " + what);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onHandleIntent: what = " + what + ", end!!!");
                break;
            default:
                Log.e(TAG, "onHandleIntent: what = " + 0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

}

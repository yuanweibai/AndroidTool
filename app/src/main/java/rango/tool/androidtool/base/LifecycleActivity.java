package rango.tool.androidtool.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

@SuppressLint("Registered")
public class LifecycleActivity extends BaseActivity {

    public static String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent()");
        setIntent(intent);
    }

    @Override protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart()");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "onAttachedToWindow()");
    }

    @Override public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "onDetachedFromWindow()");
    }
}

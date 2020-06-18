package rango.tool.androidtool.guide;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class GuideActivity extends Activity {

    private static final String TAG = "GuideActivity";

    @Override
    protected void onCreate(Bundle bundle) {
        Log.e(TAG,"onCreate()");
        ActivityHook.tryToHook(this);
        super.onCreate(bundle);

        initView();
    }

    private void initView() {
        getWindow().getDecorView().setPadding(ScreenUtils.dp2px(18), ScreenUtils.dp2px(36), ScreenUtils.dp2px(18), 0);
        LayoutParams layoutParams = getWindow().getAttributes();
        getWindow().setFlags(32, 32);
        layoutParams.gravity = 48;
        layoutParams.width = -1;

        View view = View.inflate(this, R.layout.activity_guide, null);
        setContentView(view, layoutParams);
        view.setOnClickListener(v -> finish());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,"onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"onPause()");
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG,"onStop()");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");
    }
}

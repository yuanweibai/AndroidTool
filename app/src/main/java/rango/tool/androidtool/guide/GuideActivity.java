package rango.tool.androidtool.guide;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import rango.tool.androidtool.R;

public class GuideActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        ActivityHook.m15071a(this);
        super.onCreate(bundle);

        initView();
    }

    private void initView() {
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        LayoutParams layoutParams = getWindow().getAttributes();
        getWindow().setFlags(32, 32);
        layoutParams.gravity = 48;
        layoutParams.width = -1;

        View view = View.inflate(this, R.layout.activity_guide, null);
        setContentView(view, layoutParams);
        view.setOnClickListener(v -> finish());
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}

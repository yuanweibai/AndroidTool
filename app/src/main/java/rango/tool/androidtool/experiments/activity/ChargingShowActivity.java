package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class ChargingShowActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_charging_show);
    }
}

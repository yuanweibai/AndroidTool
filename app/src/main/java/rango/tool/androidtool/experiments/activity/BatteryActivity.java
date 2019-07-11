package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.battery.BatteryBroadcastReceiver;

public class BatteryActivity extends BaseActivity {

    private BatteryBroadcastReceiver batteryBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        findViewById(R.id.register_btn).setOnClickListener(v -> batteryBroadcastReceiver = BatteryBroadcastReceiver.register(BatteryActivity.this));

        findViewById(R.id.unregister_btn).setOnClickListener(v -> {
            if (batteryBroadcastReceiver != null) {
                BatteryBroadcastReceiver.unRegister(BatteryActivity.this, batteryBroadcastReceiver);
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (batteryBroadcastReceiver != null) {
            BatteryBroadcastReceiver.unRegister(BatteryActivity.this, batteryBroadcastReceiver);
        }
    }
}

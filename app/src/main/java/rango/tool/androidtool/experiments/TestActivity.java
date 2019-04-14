package rango.tool.androidtool.experiments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.clickeffect.RippleRelativeLayout;
import rango.tool.androidtool.dialog.ToolDialog;
import rango.tool.androidtool.experiments.activity.AnrActivity;
import rango.tool.androidtool.experiments.activity.AutoScrollActivity;
import rango.tool.androidtool.experiments.activity.BroadcastActivity;
import rango.tool.androidtool.experiments.activity.ButtonActivity;
import rango.tool.androidtool.experiments.activity.CanvasActivity;
import rango.tool.androidtool.experiments.activity.NavigationBarActivity;
import rango.tool.androidtool.experiments.activity.PotholerActivity;
import rango.tool.androidtool.experiments.activity.ProgressBarActivity;
import rango.tool.androidtool.experiments.activity.ServiceActivity;
import rango.tool.androidtool.experiments.activity.ShapeActivity;
import rango.tool.androidtool.experiments.activity.StackActivity;
import rango.tool.androidtool.experiments.activity.StickerActivity;
import rango.tool.androidtool.experiments.activity.WaterMarkActivity;
import rango.tool.androidtool.experiments.activity.WindowActivity;
import rango.tool.androidtool.job.JobActivity;
import rango.tool.androidtool.keyboard.KeyboardActivity;
import rango.tool.androidtool.launchmodel.LaunchMode1Activity;
import rango.tool.androidtool.locker.LockerManager;
import rango.tool.androidtool.memoryleak.MemoryLeakActivity;
import rango.tool.androidtool.nestedscroll.NestedScrollActivity;
import rango.tool.androidtool.workmanager.WorkManagerActivity;

public class TestActivity extends BaseActivity {

    private View view;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
//                LockerManager.getInstance(TestActivity.this).lockScreen();
            } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
//                unLockScreen();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastReceiver, intentFilter);

        setContentView(R.layout.test_layout);
        findViewById(R.id.canvas_btn).setOnClickListener(v -> startActivity(CanvasActivity.class));
        findViewById(R.id.shape_btn).setOnClickListener(v -> startActivity(ShapeActivity.class));

        findViewById(R.id.locker).setOnClickListener(v -> LockerManager.getInstance(TestActivity.this).lockScreen());
        findViewById(R.id.window).setOnClickListener(v -> startActivity(WindowActivity.class));
        findViewById(R.id.navigation_btn).setOnClickListener(v -> startActivity(NavigationBarActivity.class));
        findViewById(R.id.auto_scroll_btn).setOnClickListener(v -> startActivity(AutoScrollActivity.class));
        findViewById(R.id.button_btn).setOnClickListener(v -> startActivity(ButtonActivity.class));
        findViewById(R.id.nested_scroll).setOnClickListener(v -> startActivity(NestedScrollActivity.class));
        findViewById(R.id.anr_btn).setOnClickListener(v -> startActivity(AnrActivity.class));
        findViewById(R.id.work_manager).setOnClickListener(v -> startActivity(WorkManagerActivity.class));
        findViewById(R.id.job_btn).setOnClickListener(v -> startActivity(JobActivity.class));
        findViewById(R.id.potholer_btn).setOnClickListener(v -> startActivity(PotholerActivity.class));
        findViewById(R.id.stack_btn).setOnClickListener(v -> startActivity(StackActivity.class));
        findViewById(R.id.sticker_btn).setOnClickListener(v -> {
            startActivity(StickerActivity.class);
        });

        RippleRelativeLayout layout = findViewById(R.id.click_effect_layout);
        findViewById(R.id.ripple_text).setOnClickListener(v -> {

        });

        findViewById(R.id.launch_mode_btn).setOnClickListener(v -> startActivity(LaunchMode1Activity.class));
        findViewById(R.id.memory_leak_btn).setOnClickListener(v -> startActivity(MemoryLeakActivity.class));
        findViewById(R.id.progress_btn).setOnClickListener(v -> startActivity(ProgressBarActivity.class));

        findViewById(R.id.keyboard_btn).setOnClickListener(v -> startActivity(KeyboardActivity.class));

        findViewById(R.id.service_btn).setOnClickListener(v -> startActivity(ServiceActivity.class));
        findViewById(R.id.broadcast_btn).setOnClickListener(v -> startActivity(BroadcastActivity.class));
        findViewById(R.id.dialog_btn).setOnClickListener(v -> showDialogFragment(ToolDialog.newInstance()));
        findViewById(R.id.water_mark_btn).setOnClickListener(v -> startActivity(WaterMarkActivity.class));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

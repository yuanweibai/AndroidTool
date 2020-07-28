package rango.tool.androidtool.experiments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.accessibility.AccessibilityActivity;
import rango.tool.androidtool.alarm.AlarmActivity;
import rango.tool.androidtool.alive.AliveActivity;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.clickeffect.RippleRelativeLayout;
import rango.tool.androidtool.dialog.ToolDialog;
import rango.tool.androidtool.earning.EarningActivity;
import rango.tool.androidtool.experiments.activity.AnyThingActivity;
import rango.tool.androidtool.experiments.activity.OutlineTextActivity;
import rango.tool.androidtool.farm.PigActivity;
import rango.tool.androidtool.encrypt.EncryptActivity;
import rango.tool.androidtool.experiments.activity.AidlActivity;
import rango.tool.androidtool.experiments.activity.AnrActivity;
import rango.tool.androidtool.experiments.activity.AutoScrollActivity;
import rango.tool.androidtool.experiments.activity.BatteryActivity;
import rango.tool.androidtool.experiments.activity.BroadcastActivity;
import rango.tool.androidtool.experiments.activity.ButtonActivity;
import rango.tool.androidtool.experiments.activity.CanvasActivity;
import rango.tool.androidtool.experiments.activity.DialogActivity;
import rango.tool.androidtool.experiments.activity.ExceptionActivity;
import rango.tool.androidtool.experiments.activity.GifImageActivity;
import rango.tool.androidtool.experiments.activity.NavigationBarActivity;
import rango.tool.androidtool.experiments.activity.NestedScrollRecyclerViewActivity;
import rango.tool.androidtool.experiments.activity.NotificationActivity;
import rango.tool.androidtool.experiments.activity.PotholerActivity;
import rango.tool.androidtool.experiments.activity.ProgressBarActivity;
import rango.tool.androidtool.experiments.activity.ProviderTestActivity;
import rango.tool.androidtool.experiments.activity.RippleActivity;
import rango.tool.androidtool.experiments.activity.ServiceActivity;
import rango.tool.androidtool.experiments.activity.ShapeActivity;
import rango.tool.androidtool.experiments.activity.StackActivity;
import rango.tool.androidtool.experiments.activity.StickerActivity;
import rango.tool.androidtool.experiments.activity.ThreadActivity;
import rango.tool.androidtool.experiments.activity.ViewDrawActivity;
import rango.tool.androidtool.experiments.activity.WaterMarkActivity;
import rango.tool.androidtool.experiments.activity.WindowActivity;
import rango.tool.androidtool.guide.GuideActivity;
import rango.tool.androidtool.http.activity.HttpActivity;
import rango.tool.androidtool.job.JobActivity;
import rango.tool.androidtool.keyboard.KeyboardActivity;
import rango.tool.androidtool.launchmodel.LaunchMode1Activity;
import rango.tool.androidtool.locker.LockerActivity;
import rango.tool.androidtool.locker.LockerManager;
import rango.tool.androidtool.memoryleak.MemoryLeakActivity;
import rango.tool.androidtool.nestedscroll.NestedScrollActivity;
import rango.tool.androidtool.other.OtherActivity;
import rango.tool.androidtool.video.VideoActivity;
import rango.tool.androidtool.viewpager.ViewPagerActivity;
import rango.tool.androidtool.wallpaper.WallpaperActivity;
import rango.tool.androidtool.workmanager.WorkManagerActivity;
import rango.tool.common.utils.Worker;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

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
        Log.e(TAG, "onCreate()");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastReceiver, intentFilter);

        Log.e("rango", "main process: " + android.os.Process.myPid());

        setContentView(R.layout.test_layout);
        findViewById(R.id.canvas_btn).setOnClickListener(v -> startActivity(CanvasActivity.class));
        findViewById(R.id.shape_btn).setOnClickListener(v -> startActivity(ShapeActivity.class));

        findViewById(R.id.locker).setOnClickListener(v -> LockerManager.getInstance(TestActivity.this).lockScreen());
        findViewById(R.id.locker_delay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker.postMain(LockerActivity::start, 5000);
            }
        });
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
        findViewById(R.id.notification_listen_btn).setOnClickListener(v -> startActivity(NotificationActivity.class));
        findViewById(R.id.sticker_btn).setOnClickListener(v -> {
            startActivity(StickerActivity.class);
        });

        RippleRelativeLayout layout = findViewById(R.id.click_effect_layout);
        findViewById(R.id.ripple_text).setOnClickListener(v -> {

        });

        findViewById(R.id.launch_mode_btn).setOnClickListener(v -> {
            LaunchMode1Activity.start(TestActivity.this, "TestActivity");
        });
        findViewById(R.id.memory_leak_btn).setOnClickListener(v -> startActivity(MemoryLeakActivity.class));
        findViewById(R.id.progress_btn).setOnClickListener(v -> startActivity(ProgressBarActivity.class));

        findViewById(R.id.keyboard_btn).setOnClickListener(v -> startActivity(KeyboardActivity.class));

        findViewById(R.id.service_btn).setOnClickListener(v -> startActivity(ServiceActivity.class));
        findViewById(R.id.broadcast_btn).setOnClickListener(v -> startActivity(BroadcastActivity.class));
        findViewById(R.id.dialog_btn).setOnClickListener(v -> showDialogFragment(ToolDialog.newInstance()));
        findViewById(R.id.water_mark_btn).setOnClickListener(v -> startActivity(WaterMarkActivity.class));
        findViewById(R.id.scroll_recycler_btn).setOnClickListener(v -> startActivity(NestedScrollRecyclerViewActivity.class));
        findViewById(R.id.aidl_btn).setOnClickListener(v -> startActivity(AidlActivity.class));
        findViewById(R.id.ripple_btn).setOnClickListener(v -> startActivity(RippleActivity.class));
        findViewById(R.id.exception_btn).setOnClickListener(v -> startActivity(ExceptionActivity.class));
        findViewById(R.id.battery_btn).setOnClickListener(v -> startActivity(BatteryActivity.class));
        findViewById(R.id.dialog_btn_2).setOnClickListener(v -> startActivity(DialogActivity.class));
        findViewById(R.id.provider_btn).setOnClickListener(v -> startActivity(ProviderTestActivity.class));
        findViewById(R.id.gif_image_btn).setOnClickListener(v -> startActivity(GifImageActivity.class));
        findViewById(R.id.thread_btn).setOnClickListener(v -> startActivity(ThreadActivity.class));
        findViewById(R.id.set_wallpaper_btn).setOnClickListener(v -> startActivity(WallpaperActivity.class));
        view = findViewById(R.id.view_draw_btn);
        view.setOnClickListener(v -> startActivity(ViewDrawActivity.class));

        findViewById(R.id.send_broadcast_to_other_process_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.normal.other.process.receiver");
                intent.putExtra("rango_value", 89);
                sendBroadcast(intent);
            }
        });
        findViewById(R.id.keep_alive_btn).setOnClickListener(v -> startActivity(AliveActivity.class));
        findViewById(R.id.http_btn).setOnClickListener(v -> startActivity(HttpActivity.class));
        findViewById(R.id.alarm_btn).setOnClickListener(v -> startActivity(AlarmActivity.class));
        findViewById(R.id.encrypt_btn).setOnClickListener(v -> startActivity(EncryptActivity.class));
        findViewById(R.id.drag_btn).setOnClickListener(v -> startActivity(PigActivity.class));
        findViewById(R.id.outline_text_btn).setOnClickListener(v -> startActivity(OutlineTextActivity.class));
        findViewById(R.id.other_btn).setOnClickListener(v -> startActivity(OtherActivity.class));
        findViewById(R.id.video_btn).setOnClickListener(v -> startActivity(VideoActivity.class));
        findViewById(R.id.any_thing_btn).setOnClickListener(v -> startActivity(AnyThingActivity.class));
        findViewById(R.id.acc_btn).setOnClickListener(v -> startActivity(AccessibilityActivity.class));
        findViewById(R.id.earning_anim_btn).setOnClickListener(v -> startActivity(EarningActivity.class));
        findViewById(R.id.guide_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GuideActivity.class);
            }
        });

        findViewById(R.id.view_pager_btn).setOnClickListener(v -> startActivity(ViewPagerActivity.class));

    }

    Handler handler = new Handler();


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");

        handler.post(() -> {
            int height = view.getHeight();
            int width = view.getWidth();

            Log.e("rango", "handler: height = " + height + ", width = " + width);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy()");
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

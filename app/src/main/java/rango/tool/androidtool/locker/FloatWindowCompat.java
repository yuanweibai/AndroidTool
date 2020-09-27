package rango.tool.androidtool.locker;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import rango.tool.common.utils.ScreenUtils;

/**
 * Compatibility utilities for lock screen float window.
 */
public class FloatWindowCompat {

    static WindowManager.LayoutParams getLockScreenParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (needsSystemErrorFloatWindow()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            }
            layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            layoutParams.systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            layoutParams.systemUiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            int phoneWidth = ScreenUtils.getScreenWidthPx();
            int phoneHeight = ScreenUtils.getScreenHeightPx();
            layoutParams.width = Math.min(phoneWidth, phoneHeight);
            layoutParams.height = Math.max(phoneWidth, phoneHeight);
        }
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        layoutParams.gravity = Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        return layoutParams;
    }

    public static boolean needsSystemErrorFloatWindow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}

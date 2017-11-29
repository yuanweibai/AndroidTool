package rango.tool.common.utils;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class WindowUtil {

    public static WindowManager.LayoutParams getLockScreenParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        if (needsSystemErrorFloatWindow()) {
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        layoutParams.systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        } else {
//            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//            int phoneWidth = CommonUtils.getPhoneWidth(HSApplication.getContext());
//            int phoneHeight = CommonUtils.getPhoneHeight(HSApplication.getContext());
//            layoutParams.width = phoneWidth < phoneHeight ? phoneWidth : phoneHeight;
//            layoutParams.height = phoneWidth > phoneHeight ? phoneWidth : phoneHeight;
//        }
//        layoutParams.format = PixelFormat.TRANSPARENT;
//        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        layoutParams.gravity = Gravity.TOP;
//        layoutParams.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        }
        return layoutParams;
    }

    public static WindowManager.LayoutParams getLockScreenParams2() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        layoutParams.systemUiVisibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        layoutParams.systemUiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

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

}

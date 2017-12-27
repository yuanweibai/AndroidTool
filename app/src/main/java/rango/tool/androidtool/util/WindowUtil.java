package rango.tool.androidtool.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

import rango.tool.androidtool.R;
import rango.tool.androidtool.view.WindowInsetsLayout;
import rango.tool.common.utils.ReflectionHelper;

public class WindowUtil {

    //Cache variables
    private static int NAVIGATION_BAR_HEIGHT = -1;
    private static int STATUS_BAR_HEIGHT = -1;

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

    public static int getNavigationBarHeight(Context context) {
        if (null == context) {
            return 0;
        }
        if (NAVIGATION_BAR_HEIGHT > -1) {
            return NAVIGATION_BAR_HEIGHT;
        }
        if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Activity activityContext = (Activity) context;
            DisplayMetrics metrics = new DisplayMetrics();
            activityContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activityContext.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight) {
                NAVIGATION_BAR_HEIGHT = realHeight - usableHeight;
            }
            return NAVIGATION_BAR_HEIGHT;
        }
        NAVIGATION_BAR_HEIGHT = getNavigationBarHeightUnconcerned(context);
        return NAVIGATION_BAR_HEIGHT;
    }

    private static int getNavigationBarHeightUnconcerned(Context context) {
        if (null == context) {
            return 0;
        }
        Resources localResources = context.getResources();
        if (!hasNavigationBar(context)) {
            return 0;
        }
        int i = localResources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (i > 0) {
            return localResources.getDimensionPixelSize(i);
        }
        i = localResources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
        if (i > 0) {
            return localResources.getDimensionPixelSize(i);
        }
        return 0;
    }

    private static boolean hasNavigationBar(Context paramContext) {
        boolean bool = true;
        String sNavBarOverride;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class klass = ReflectionHelper.getClass("android.os.SystemProperties");
                Object localObject = ReflectionHelper.getDeclaredMethod(klass, "get", String.class);
                ((Method) localObject).setAccessible(true);
                sNavBarOverride = (String) ((Method) localObject).invoke(null, "qemu.hw.mainkeys");
                localObject = paramContext.getResources();
                int i = ((Resources) localObject).getIdentifier("config_showNavigationBar", "bool", "android");
                if (i != 0) {
                    bool = ((Resources) localObject).getBoolean(i);
                    if ("1".equals(sNavBarOverride)) {
                        return false;
                    }
                }
            } catch (Throwable localThrowable) {
            }
        }

        if (!ViewConfiguration.get(paramContext).hasPermanentMenuKey()) {
            return bool;
        }

        return false;
    }

    /**
     * Sets up transparent navigation and status bars in LMP.
     * This method is a no-op for other platform versions.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void immersiveStatusAndNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.getAttributes().systemUiVisibility |= (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                    .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Sets up transparent status bars in LMP.
     * This method is a no-op for other platform versions.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void immersiveStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.getAttributes().systemUiVisibility |= (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void immersiveStatusAndNavigationBar(Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static int getStatusBarHeight() {
        if (STATUS_BAR_HEIGHT == -1) {
            Resources resources = Resources.getSystem();

            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                STATUS_BAR_HEIGHT = resources.getDimensionPixelSize(resourceId);
            }
        }
        return STATUS_BAR_HEIGHT;
    }

    /**
     * 当状态栏的背景色是白色的时候：
     * 如果状态栏的文字、icon也是白色的，就得改变其文字、icon的颜色，且此功能只有在 sdk >= 23 才有此方法：decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
     * sdk < 23 只能改变其背景色了；
     *
     * @param activity
     */
    public static void setStatusIconColorForWhiteBg(Activity activity) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = Color.WHITE;
            View decor = activity.getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            color = Color.BLACK;
        }
        setStatusBarColor(activity, color);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    public static void hideStatusBar(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void showStatusBar(Activity activity) {
        final Window window = activity.getWindow();
        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) ==
                WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void setNavigationBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        } else {
            setNavigationBarColorNative(activity, color);
        }
    }

    /**
     * 此方法是在API 21以下用的
     * 就是通过把虚拟按键设置成透明的，然后在虚拟按键下添加一个View，再给此 View 设置颜色就可以了
     *
     * @param activity
     * @param color
     */
    public static void setNavigationBarColorNative(Activity activity, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            View navigationBarView = activity.findViewById(R.id.navigation_bar_bg_v);
            if (null != navigationBarView) {
                if (color == Color.TRANSPARENT) {
                    navigationBarView.setVisibility(View.GONE);
                } else {
                    int navigationBarHeight = getNavigationBarHeight(activity);
                    if (navigationBarHeight == 0) {
                        navigationBarView.setVisibility(View.GONE);
                    } else {
                        WindowInsetsLayout.LayoutParams params = new WindowInsetsLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navigationBarHeight);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        params.mInsetWay = WindowInsetsLayout.LayoutParams.InsetWay.NONE;
                        navigationBarView.setLayoutParams(params);
                        navigationBarView.setBackgroundColor(color);
                        navigationBarView.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

}

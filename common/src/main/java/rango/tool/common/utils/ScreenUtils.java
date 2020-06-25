package rango.tool.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ScreenUtils {
    private static final String TAG = ScreenUtils.class.getSimpleName();

    private static final Map<Float, Integer> DP_TO_PX_CACHE = new HashMap<>(50);
    private static int SCREEN_WIDTH_PX_CACHE = -1;
    private static int SCREEN_HEIGHT_PX_CACHE = -1;
    private static int SCREEN_WIDTH_DP_CACHE = -1;
    private static int SCREEN_HEIGHT_DP_CACHE = -1;
    private static float SCREEN_ONE_DP_TO_PX = -1;
    private static float SCREEN_ONE_PX_TO_DP = -1;
    private static int STATUS_BAR_HEIGHT = -1;

    private static TextPaint textPaint;

    public static int dp2px(float dp) {
        if (DP_TO_PX_CACHE.containsKey(dp)) {
            return DP_TO_PX_CACHE.get(dp);
        }

        if (SCREEN_ONE_DP_TO_PX < 0) {
            SCREEN_ONE_DP_TO_PX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, Resources.getSystem().getDisplayMetrics());
        }

        int ret = (int) (SCREEN_ONE_DP_TO_PX * dp);

        DP_TO_PX_CACHE.put(dp, ret);

        return ret;
    }

    public static int px2dp(float px) {
        if (SCREEN_ONE_PX_TO_DP < 0) {
            Resources resources = Resources.getSystem();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            SCREEN_ONE_PX_TO_DP = (1 / (metrics.densityDpi / 160f));
        }

        return (int) (SCREEN_ONE_PX_TO_DP * px);
    }

    public static int px2sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidthPx() {
        if (SCREEN_WIDTH_PX_CACHE < 0) {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

            SCREEN_WIDTH_PX_CACHE = metrics.widthPixels;

//            //fix class cast exception
//            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            Display display = windowManager.getDefaultDisplay();
//            SCREEN_WIDTH_PX_CACHE = display.getWidth();
        }

        return SCREEN_WIDTH_PX_CACHE;
    }

    public static int getScreenHeightPx() {
        if (SCREEN_HEIGHT_PX_CACHE < 0) {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            SCREEN_HEIGHT_PX_CACHE = metrics.heightPixels;

//            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            Display display = wm.getDefaultDisplay();
//            SCREEN_HEIGHT_PX_CACHE = display.getHeight();
        }

        return SCREEN_HEIGHT_PX_CACHE;
    }

    public static Point getScreenDimensionsInDp(Context context) {
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Configuration configuration = context.getResources().getConfiguration();
			return new Point(configuration.screenWidthDp, configuration.screenHeightDp);
		} else {
		*/

        if (SCREEN_WIDTH_DP_CACHE < 0 || SCREEN_HEIGHT_DP_CACHE < 0) {
            // APIs prior to v13 gave the screen dimensions in pixels. We convert them to DIPs before returning them.
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            SCREEN_WIDTH_DP_CACHE = px2dp(displayMetrics.widthPixels);
            SCREEN_HEIGHT_DP_CACHE = px2dp(displayMetrics.heightPixels);
        }

        return new Point(SCREEN_WIDTH_DP_CACHE, SCREEN_HEIGHT_DP_CACHE);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);

            InputMethodManager inputMethodManager = (InputMethodManager) activityWeakReference.get().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activityWeakReference.get().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * use {@link android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE } for auto brightness,
//     * use {@link android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL } for full brightness,
//     * use {@link android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF } for low brightness
//     */
//    public static void adjustScreenBrightness(ActivityUtil context, float brightness) {
//        try {
//            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//            lp.screenBrightness = brightness;
//            context.getWindow().setAttributes(lp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * get text font size
     *
     * @param name
     * @return
     */
    public static float getTextWidth(String name) {
        return getTextWidth(name, 12f);//default font size is 12sp
    }

    /**
     * 此方法有缺陷，当text中有汉子和字符混合使用时，计算的宽不准确；
     *
     * @param text
     * @param fontSize
     * @return
     */

    public static float getTextWidth(String text, float fontSize) {
        if (textPaint == null) {
            textPaint = new TextPaint();
        }
        textPaint.setTextSize(ScreenUtils.dp2px(fontSize));

        float textWidth = Layout.getDesiredWidth(text, textPaint);
        // 此方法也可以
//        float textWidth = textPaint.measureText(text);
        return textWidth;
    }

    /**
     * 此方法可以弥补{@link #getTextWidth(String, float)}的缺陷
     *
     * @param text
     * @param textPaint 可以公共{@link TextView#getPaint()}来获取
     * @return
     */
    public static float getTextWidth(String text, TextPaint textPaint) {
//        return Layout.getDesiredWidth(text,textPaint); //此方法也行
        return textPaint.measureText(text);
    }


    public static int getTextHeight(float fontSize) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (fontSize * fontScale + 0.5f);
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
     * 获取屏幕尺寸，单位为英寸
     * 计算屏幕尺寸应该使用精确密度：xdpi ydpi来计算
     * 使用归一化密度：densitydpi是错误的，它是固定值，
     * 120 160 240 320 480,根据dp计算像素才使用它
     *
     * @param context
     * @return
     */
    public static double getScreenSizeInInch(Activity context) {
        if (Build.VERSION.SDK_INT >= 17) {
            Point point = new Point();
            context.getWindowManager().getDefaultDisplay().getRealSize(point);

            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            double x = Math.pow(point.x / dm.xdpi, 2);
            double y = Math.pow(point.y / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
//            System.out.println("qqqqqqq=="+screenInches);
            return screenInches;
        } else {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            double heightInInch = (double) dm.heightPixels / (double) (dm.densityDpi);
            double widthInInch = (double) dm.widthPixels / (double) (dm.densityDpi);
            double screenSizeInInch = Math.sqrt(heightInInch * heightInInch
                    + widthInInch * widthInInch);
//            System.out.println("qqqqqq==" + dm.heightPixels + "," + dm.ydpi + "," + dm.density + "," + dm.widthPixels + "," + dm.xdpi + "," + dm.densityDpi);
//            System.out.println("qqqqqq==高：" + heightInInch + " 宽：" + widthInInch + " 尺寸:" + screenSizeInInch
//                    + " 单位（英寸)");
            return screenSizeInInch;
        }
    }

//    public static int initSmartBarHeight(Context context) {
//        String manufacturer = Build.MANUFACTURER.toUpperCase(Locale.US);
//        int year = YearClass.get(context);
//        int height = 0;
//
//        Log.d(TAG, "Current device info = Manufacturer: " + manufacturer + " Year: " + year);
//
//        if (manufacturer.equals("MEIZU")) {
//            if (year <= YearClass.CLASS_2014) {
//                height = 192;
//            }
//        }
//
//        return height;
//    }

    public static int getRealScreenHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = ReflectionUtils.getClassForName();
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    public static int getContentScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getNavigationBarHeight(Context context) {
        int totalHeight = getRealScreenHeight(context);

        int contentHeight = getContentScreenHeight(context);

        return totalHeight - contentHeight;
    }

    public static int getNavigationBarHeightForPopup(Context context) {
        if ((Build.MANUFACTURER.equalsIgnoreCase("HTC") || Build.MANUFACTURER.equalsIgnoreCase("MediaTek MEEG 308")) && (Build.VERSION.SDK_INT == 21)) {
            return ScreenUtils.getNavigationBarHeight(context);
        }

        return 0;
    }

    public static boolean isFullScreen(final Activity activity) {
        return ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0)
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && (activity.getWindow().getDecorView().getSystemUiVisibility() == (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN)));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isTranslucentStatus(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (activity.getWindow().getAttributes().flags &
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isFitsSystemWindows(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
            return viewGroup != null && viewGroup.getChildAt(0).getFitsSystemWindows();
        }

        return false;
    }

    /**
     * Set full screen for MainActivity. Be careful to modify it!!!
     *
     * @param activity
     * @param isFullScreen
     */
    public static void setFullScreen(final Activity activity, boolean isFullScreen) {
        if (isFullScreen) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void adjustMainActUiVisibility(final Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    public static void setCustomDensity(Activity activity, final Application application) {
        final DisplayMetrics metrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = metrics.density;
            sNoncompatScaledDensity = metrics.scaledDensity;

            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        final float targetDensity = metrics.widthPixels / 360f;
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        metrics.density = targetDensity;
        metrics.scaledDensity = targetScaledDensity;
        metrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityMetrics = activity.getResources().getDisplayMetrics();
        activityMetrics.density = targetDensity;
        activityMetrics.scaledDensity = targetScaledDensity;
        activityMetrics.densityDpi = targetDensityDpi;
    }
}

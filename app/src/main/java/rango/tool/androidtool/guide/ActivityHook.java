package rango.tool.androidtool.guide;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivityHook {

    public static void tryToHook(Activity activity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hookOrientation : targetSdkVersion=");
        stringBuilder.append(activity.getApplicationInfo().targetSdkVersion);
        stringBuilder.append(",Build.VERSION_CODES.O=");
        stringBuilder.append(26);
        stringBuilder.append(",Build.VERSION.SDK_INT=");
        stringBuilder.append(Build.VERSION.SDK_INT);
        if (activity.getApplicationInfo().targetSdkVersion >= 26 && Build.VERSION.SDK_INT == 26 && ActivityHook.hookTranslucentOrFloating(activity)) {
            ActivityHook.hookScreenOrientation(activity);
        }
    }

    private static void hookScreenOrientation(Activity activity) {
        try {
            Field declaredField = Activity.class.getDeclaredField("mActivityInfo");
            declaredField.setAccessible(true);
            ((ActivityInfo) declaredField.get(activity)).screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean hookTranslucentOrFloating(Activity activity) {
        boolean z = false;
        try {
            Field declaredField = Class.forName("com.android.internal.R$styleable").getDeclaredField("Window");
            declaredField.setAccessible(true);
            TypedArray obtainStyledAttributes = activity.obtainStyledAttributes((int[]) declaredField.get(null));
            Method declaredMethod = ActivityInfo.class.getDeclaredMethod("isTranslucentOrFloating", new Class[]{TypedArray.class});
            declaredMethod.setAccessible(true);
            z = ((Boolean) declaredMethod.invoke(null, new Object[]{obtainStyledAttributes})).booleanValue();
            return z;
        } catch (Exception e) {
            e.printStackTrace();
            return z;
        }
    }
}

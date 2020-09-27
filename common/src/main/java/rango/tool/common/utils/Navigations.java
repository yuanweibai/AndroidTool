package rango.tool.common.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.List;
import java.util.NoSuchElementException;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;

/**
 * Utility class for navigating through activities.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Navigations {

    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

    /**
     * Start an activity on the given context, {@link Intent#FLAG_ACTIVITY_NEW_TASK} will be added to intent when the
     * given context is not an activity context.
     */
    public static void startActivity(Context context, Class<?> klass) {
        Intent intent = new Intent(context, klass);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivitySafely(Context context, Class<?> klass) {
        Intent intent = new Intent(context, klass);
        startActivitySafely(context, intent);
    }

    /**
     * Start an activity with a minor delay, avoid blocking animation playing on the previous activity
     * (eg. material ripple effect above Lollipop).
     */
    public static void startActivityMainThreadFriendly(final Context context, Class<?> klass) {
        startActivityMainThreadFriendly(context, new Intent(context, klass));
    }

    public static void startActivityMainThreadFriendly(final Context context, final Intent intent) {
        sMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(intent);
            }
        }, 300);
    }

    public static void startActivitySafelyAndClearTask(Context context, Intent intent) {
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (RuntimeException e) {
        }
    }

    public static void startActivitySafely(Context context, Intent intent) {
        try {
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (RuntimeException e) {
        }
    }

    public static void startActivitiesSafely(Context context, Intent[] intents) {
        try {
            context.startActivities(intents);
        } catch (RuntimeException e) {
        }
    }

    /**
     * Start a system settings page with the given action.
     *
     * @param action            Action to start. Eg. {@link Settings#ACTION_DISPLAY_SETTINGS}.
     * @param attachPackageName If package name of this launcher should be attached by {@link Intent#setData(Uri)}.
     */
    public static void startSystemSetting(Context activityContext, String action, boolean attachPackageName) {
        Intent intent = new Intent(action);
        if (attachPackageName) {
            intent.setData(Uri.parse("package:" + activityContext.getPackageName()));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivitySafely(activityContext, intent);
    }


}

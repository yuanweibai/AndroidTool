package rango.tool.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;

public class ActivityUtil {

    public static Activity contextToActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return (Activity) (((ContextThemeWrapper) context).getBaseContext());
        } else if (context instanceof android.support.v7.view.ContextThemeWrapper) {
            return (Activity) (((android.support.v7.view.ContextThemeWrapper) context).getBaseContext());
        } else {
            return null;
        }
    }
}

package rango.tool.androidtool.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by baiyuanwei on 17/11/15.
 */

public class BaseApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        context = base;
    }
}

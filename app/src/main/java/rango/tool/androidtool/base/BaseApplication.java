package rango.tool.androidtool.base;

import android.app.Application;
import android.content.Context;

import rango.kotlin.wanandroid.common.utils.WAApplication;

/**
 * Created by baiyuanwei on 17/11/15.
 */

public class BaseApplication extends Application {

    static Context context;

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
        WAApplication.INSTANCE.attachBaseContext(this);

        context = base;
    }
}

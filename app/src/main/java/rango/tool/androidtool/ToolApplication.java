package rango.tool.androidtool;

import rango.tool.androidtool.base.BaseApplication;
import rango.tool.common.utils.Worker;

public class ToolApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        Worker.destroy();
        super.onTerminate();
    }
}

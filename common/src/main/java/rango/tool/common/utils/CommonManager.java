package rango.tool.common.utils;

import android.content.Context;
import android.support.annotation.NonNull;

public class CommonManager {

    public interface CommonCallable {

        Context getApplicationContext();
    }

    private @NonNull CommonCallable callable;

    private CommonManager() {
    }

    private static class ClassHolder {
        private static final CommonManager sInstance = new CommonManager();
    }

    public static CommonManager getInstance() {
        return ClassHolder.sInstance;
    }

    public void setCommonCallable(@NonNull CommonCallable callable) {
        this.callable = callable;
    }

    public @NonNull Context getApplicationContext() {
        return callable.getApplicationContext();
    }
}

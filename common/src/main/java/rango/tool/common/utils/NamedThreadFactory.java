package rango.tool.common.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
    private String prefix = "tool-pool";
    private int counter = 0;

    NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        return new Thread(runnable, prefix + "-" + counter++);
    }
}

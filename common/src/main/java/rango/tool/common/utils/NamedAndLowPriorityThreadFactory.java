package rango.tool.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedAndLowPriorityThreadFactory implements ThreadFactory {

    private final ThreadFactory mDefaultThreadFactory;
    private final String mBaseName;
    private final AtomicInteger mCount = new AtomicInteger(0);

    public NamedAndLowPriorityThreadFactory(final String baseName) {
        mDefaultThreadFactory = Executors.defaultThreadFactory();
        mBaseName = baseName;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = mDefaultThreadFactory.newThread(runnable);
        thread.setName(mBaseName + "-" + mCount.getAndIncrement());
        return thread;
    }

}

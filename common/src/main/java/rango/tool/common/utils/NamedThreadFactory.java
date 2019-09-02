package rango.tool.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private ThreadFactory defaultFactory;
    private AtomicInteger count = new AtomicInteger(0);
    private String baseName;

    public NamedThreadFactory(String baseName) {
        defaultFactory = Executors.defaultThreadFactory();
        this.baseName = baseName;

    }

    @Override
    public Thread newThread(Runnable r) {
        Thread result = defaultFactory.newThread(r);
        result.setName(baseName + "-" + count.getAndIncrement());
        return result;
    }

    public int getCreatedThreadCount() {
        return count.get();
    }
}

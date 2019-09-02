package rango.tool.common.utils;

import android.os.Process;

public abstract class LowPriorityRunnable implements Runnable {

    abstract void runThing();

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        runThing();
    }
}

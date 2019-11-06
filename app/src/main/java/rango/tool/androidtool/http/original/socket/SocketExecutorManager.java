package rango.tool.androidtool.http.original.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class SocketExecutorManager {

    private Executor callbackExecutor;
    private ExecutorService workerExecutor;

    private SocketExecutorManager() {
    }

    private static class ClassHolder {
        private final static SocketExecutorManager S_INTANCE = new SocketExecutorManager();
    }

    public static SocketExecutorManager getInstance() {
        return ClassHolder.S_INTANCE;
    }

    public Executor getCallbackExecutor() {
        if (callbackExecutor == null) {
            callbackExecutor = SocketExecutor.getMainThreadExecutor();
        }
        return callbackExecutor;
    }

    public ExecutorService getWorkerExecutorService() {
        if (workerExecutor == null) {
            workerExecutor = SocketExecutor.getWorkerExecutorService();
        }
        return workerExecutor;
    }
}

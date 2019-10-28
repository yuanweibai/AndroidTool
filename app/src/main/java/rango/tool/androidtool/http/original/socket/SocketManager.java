package rango.tool.androidtool.http.original.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class SocketManager {

    private Executor callbackExecutor;
    private ExecutorService workerExecutor;

    private SocketManager() {
    }

    private static class ClassHolder {
        private final static SocketManager S_INTANCE = new SocketManager();
    }

    public static SocketManager getInstance() {
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

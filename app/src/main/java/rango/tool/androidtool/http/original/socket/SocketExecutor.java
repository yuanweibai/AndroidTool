package rango.tool.androidtool.http.original.socket;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import rango.tool.common.utils.NamedThreadFactory;

public class SocketExecutor {

    public static Executor getMainThreadExecutor() {
        return new MainThreadExecutor();
    }

    public static ExecutorService getWorkerExecutorService() {
        return new ThreadPoolExecutor(0,
                Integer.MAX_VALUE,
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory("SocketHttp"));
    }

    private static class MainThreadExecutor implements Executor {

        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);

        }
    }


}

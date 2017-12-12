package rango.tool.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Worker {
    private static final String TAG = "Worker";
    private static ExecutorService EXECUTOR;
    private static Handler handlerMain;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void destroy() {
        EXECUTOR.shutdown();

        try {
            if (!EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (Exception e) {
            EXECUTOR.shutdownNow();

            Thread.currentThread().interrupt();
        }
    }

    public static void postWorker(Runnable r) {
        checkInit();

        try {
            EXECUTOR.execute(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postMain(Runnable r) {
        checkInit();

        if (handlerMain != null) {
            handlerMain.post(r);
        }
    }

    public static void postMainNow(Runnable r) {
        checkInit();

        if (handlerMain != null) {
            handlerMain.postAtFrontOfQueue(r);
        }
    }

    public static ExecutorService getExecutorService() {
        checkInit();

        return EXECUTOR;
    }

    public static void postMain(Runnable r, long delay) {
        checkInit();
        if (handlerMain != null) {
            handlerMain.postDelayed(r, delay);
        }
    }

    public static void removeMain(Runnable r) {
        if (handlerMain != null) {
            handlerMain.removeCallbacks(r);
        }
    }

    private static void checkInit() {
        if (EXECUTOR == null || EXECUTOR.isTerminated()) {
            EXECUTOR = Executors.newCachedThreadPool(new NamedThreadFactory("tool-worker"));
            handlerMain = new Handler(Looper.getMainLooper());
            Log.d(TAG, "check init cores:" + NUMBER_OF_CORES);
        }
    }
}

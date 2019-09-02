package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.common.utils.NamedThreadFactory;

public class ThreadActivity extends BaseActivity {

    private static final String TAG = "ThreadActivity";

    private Thread thread;

    private LinkedBlockingDeque<Runnable> workQueue;
    private NamedThreadFactory threadFactory;
    private ThreadPoolExecutor executor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_layout);

        initThreadPool();

        findViewById(R.id.start_btn).setOnClickListener(v -> startThread());

        findViewById(R.id.stop_btn).setOnClickListener(v -> stopThread());


    }

    private void initThreadPool() {
        workQueue = new LinkedBlockingDeque<>(5);
        threadFactory = new NamedThreadFactory("rango");

        executor = new ThreadPoolExecutor(3, 6, 0, TimeUnit.SECONDS, workQueue, threadFactory);
//        executor.allowCoreThreadTimeOut(true);

    }

    private void startThread() {
        if (thread == null) {
            thread = new Thread() {

                private boolean isFlag = true;

                @Override
                public void run() {


                    int i = 0;

                    Log.e(TAG, "startThread: ---------start....");


                    Log.e(TAG, "startThread: ---------end....");
                }
            };
        }

        thread.start();
    }

    private void startThread2() {
        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (this.isInterrupted()) {
                            Log.e(TAG, "interrupt 停止");
                            return;
                        }
                        if (false) {
                            throw new InterruptedException();
                        }
                        Log.e(TAG, "startThread: ---------");
                    } catch (InterruptedException e) {
                        Log.e(TAG, "InterruptedException: " + e.getMessage());
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        thread.start();
    }

    private void startThread3() {
        thread = new Thread(new Runnable() {

            private boolean isFlag = true;

            @Override
            public void run() {
                while (isFlag) {
                    Log.e(TAG, "startThread: ---------");
                }
            }
        });
        thread.start();
    }


    private void stopThread() {
//        Log.e(TAG, "stopThread1111111: isInterrupted = " + thread.getState());
//        thread.interrupt();
//        Log.e(TAG, "stopThread2222222: isInterrupted = " + thread.isInterrupted());
        thread = null;
    }

    private void threadPoolTest() {
        for (int i = 0; i < 30; i++) {

            final int k = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        Log.e(TAG, "threadPoolTest: " + Thread.currentThread().getName() + "-----" + k);
                        Thread.sleep(8000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void printThreadNum() {
        Log.e(TAG, "deque.size = " + workQueue.size());
        Log.e(TAG, "threadCount = " + threadFactory.getCreatedThreadCount());
        Log.e(TAG, "ActiveCount = " + executor.getPoolSize());


    }
}

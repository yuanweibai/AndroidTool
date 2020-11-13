package rango.kotlin.wanandroid.common.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.MessageQueue
import android.util.Log
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object Threads {

    private const val TAG = "AndroidToolThreads"

    private const val THREAD_TAG_POOL = "rango-pool-thread-"
    private const val THREAD_TAG_SINGLE = "rango-single-thread"
    private val NUMBER_OF_ALIVE_CORES = Runtime.getRuntime().availableProcessors()

    private var sExecutor: ThreadPoolExecutor? = null
    private val sDefaultThreadFactory = Executors.defaultThreadFactory()
    private var sSingleThreadExecutor: Executor
    private val sMainHandler = Handler(Looper.getMainLooper())

    fun postWorker(r: Runnable) {
        sExecutor!!.execute(r)
    }

    fun postOnSingleWorker(r: Runnable) {
        sSingleThreadExecutor.execute(r)
    }

    fun postMain(r: Runnable) {
        sMainHandler.post(r)
    }

    fun postMain(r: Runnable, delay: Long) {
        sMainHandler.postDelayed(r, delay)
    }

    fun removeOnMainThread(r: Runnable) {
        sMainHandler.removeCallbacks(r)
    }

    fun removeAllCallbackAndMessage() {
        sMainHandler.removeCallbacksAndMessages(null)
    }

    fun runOnMainThread(r: Runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // If we are on the worker thread, post onto the main handler
            postMain(r)
        } else {
            r.run()
        }
    }

    fun runOnMainThreadIdle(action: () -> Unit, timeoutMills: Long) {
        val idleHandler = MessageQueue.IdleHandler {
            Log.d(TAG, "run action when handler idle ......")
            sMainHandler.removeCallbacksAndMessages(null) // remove timeout action
            action()
            return@IdleHandler false
        }

        fun setupIdleHandler(messageQueue: MessageQueue) {
            sMainHandler.postDelayed({
                Log.d(TAG, "run action because of timeout ......")
                messageQueue.removeIdleHandler(idleHandler)
                action()
            }, timeoutMills)
            messageQueue.addIdleHandler(idleHandler)
        }

        if (Looper.getMainLooper() == Looper.myLooper()) {
            setupIdleHandler(Looper.myQueue())
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setupIdleHandler(Looper.getMainLooper().queue)
            } else {
                sMainHandler.post {
                    setupIdleHandler(Looper.myQueue())
                }
            }
        }
    }

    init {
        val poolSize = 2.coerceAtLeast(NUMBER_OF_ALIVE_CORES * 2 - 1)
        sExecutor = ThreadPoolExecutor(
                poolSize,
                poolSize,
                1,
                TimeUnit.SECONDS,
                LinkedBlockingDeque<Runnable>(),
                object : ThreadFactory {
                    private val mThreadCount = AtomicInteger(0)
                    override fun newThread(r: Runnable): Thread {
                        val thread = sDefaultThreadFactory.newThread(r)
                        thread.name = THREAD_TAG_POOL + mThreadCount.getAndIncrement()
                        thread.priority = Thread.MIN_PRIORITY
                        return thread
                    }
                }
        )
        sSingleThreadExecutor = Executors.newSingleThreadExecutor { r ->
            val thread = sDefaultThreadFactory.newThread(r)
            thread.name = THREAD_TAG_SINGLE
            thread.priority = Thread.MIN_PRIORITY
            thread
        }
    }
}
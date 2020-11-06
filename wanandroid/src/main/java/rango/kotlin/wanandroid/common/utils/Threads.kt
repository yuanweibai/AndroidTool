package rango.kotlin.wanandroid.common.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object Threads {

    private const val THREAD_TAG_POOL = "rango-pool-thread-"
    private const val THREAD_TAG_SINGLE = "rango-single-thread"
    private val NUMBER_OF_ALIVE_CORES = Runtime.getRuntime().availableProcessors()

    private var sExecutor: ThreadPoolExecutor? = null
    private val sDefaultThreadFactory = Executors.defaultThreadFactory()
    private var sSingleThreadExecutor: Executor? = null
    private val sMainHandler = Handler(Looper.getMainLooper())

    fun postWorker(r: Runnable?) {
        sExecutor!!.execute(r)
    }

    fun postOnSingleWorker(r: Runnable?) {
        sSingleThreadExecutor!!.execute(r)
    }

    fun postMain(r: Runnable?) {
        sMainHandler.post(r!!)
    }

    fun postMain(r: Runnable?, delay: Long) {
        sMainHandler.postDelayed(r!!, delay)
    }

    fun removeOnMainThread(r: Runnable?) {
        sMainHandler.removeCallbacks(r!!)
    }

    fun runOnMainThread(r: Runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // If we are on the worker thread, post onto the main handler
            postMain(r)
        } else {
            r.run()
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
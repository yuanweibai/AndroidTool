package rango.kotlin.currentactivity

import android.app.ActivityManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

class WatchingActivityService : Service() {

    companion object {
        const val TAG = "WatchingActivityService"
    }


    private var activityManager: ActivityManager? = null
    private var timer: Timer? = null

    inner class MyTimerTask : TimerTask() {

        override fun run() {
            val componentName = if (activityManager == null) {
                null
            } else {
                activityManager!!.getRunningTasks(1)[0].topActivity
            }
            val msg = componentName?.packageName + ", ${componentName?.className}"
            Log.e(TAG, "currentActivity: $msg")
        }
    }

    override fun onCreate() {
        super.onCreate()

        activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager?
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (timer == null) {
            timer = Timer()
            timer?.scheduleAtFixedRate(MyTimerTask(), 0, 1000)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
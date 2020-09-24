package rango.kotlin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.Nullable

class AppLifecycleCallback : Application.ActivityLifecycleCallbacks {
    private var visibleActivityCount = 0
    override fun onActivityCreated(@NonNull activity: Activity, @Nullable savedInstanceState: Bundle) {
        val className = activity.javaClass.name
        val isOtherLibActivity = !className.startsWith(activity.packageName) && !className.startsWith("com.infini.pigfarm")
        if (isOtherLibActivity) {
            return
        }
    }

    override fun onActivityStarted(@NonNull activity: Activity) {
        ++visibleActivityCount
        if (visibleActivityCount == 1) {
            toForeground()
        }
    }

    override fun onActivityResumed(@NonNull activity: Activity) {}
    override fun onActivityPaused(@NonNull activity: Activity) {}
    override fun onActivityStopped(@NonNull activity: Activity) {
        --visibleActivityCount
        if (visibleActivityCount == 0) {
            toBackground()
        }
    }

    override fun onActivitySaveInstanceState(@NonNull activity: Activity, @NonNull outState: Bundle) {}
    override fun onActivityDestroyed(@NonNull activity: Activity) {}
    private fun toBackground() {

    }
    private fun toForeground() {

    }

    companion object {
        private const val TAG = "AppLifecycleCallback"
    }
}
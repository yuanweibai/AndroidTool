package rango.kotlin.utils

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import rango.tool.androidtool.ToolApplication

object Devices {

    fun logMemoryInfo() {
        val rt = Runtime.getRuntime()
        val maxMemory = rt.maxMemory() / (1024 * 1024)
        Log.e("App-MaxMemory:", maxMemory.toString())
        val activityManager: ActivityManager = (ToolApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE)) as ActivityManager
        Log.e("App-MemoryClass:", activityManager.memoryClass.toString())
        Log.e("App-LargeMemoryClass:", activityManager.largeMemoryClass.toString())


        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        Log.e("System-totalMemory:", (memoryInfo.totalMem / (1024 * 1024)).toString())
        Log.e("System-availMemory:", (memoryInfo.availMem / (1024 * 1024)).toString())
        Log.e("System-threshold:", (memoryInfo.threshold / (1024 * 1024)).toString())
        Log.e("System-lowMemory:", memoryInfo.lowMemory.toString())
    }
}
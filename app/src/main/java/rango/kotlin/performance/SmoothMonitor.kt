package rango.kotlin.performance

import android.os.Looper

object SmoothMonitor {

    fun install(){
        Looper.getMainLooper().setMessageLogging(LooperPrinter())
    }
}
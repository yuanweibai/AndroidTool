package rango.kotlin.performance

import android.os.SystemClock
import android.util.Log
import android.util.Printer
import java.lang.StringBuilder

class LooperPrinter : Printer {

    companion object {
        private const val PREFIX_MSG_START = ">>>>> Dispatching to Handler"
        private const val PREFIX_MSG_END = "<<<<< Finished to Handler"
        private const val DEFAULT_START_MSG_MILLS: Long = -1
        private const val BLOCK_TIME_MILLS = 16
    }

    private var startMsgMills: Long = DEFAULT_START_MSG_MILLS

    override fun println(x: String?) {
        if (x == null || x == "") {
            return
        }
        if (x.startsWith(PREFIX_MSG_START)) {
            startMsgMills = SystemClock.uptimeMillis()
        } else if (x.startsWith(PREFIX_MSG_END)) {
            if (startMsgMills == DEFAULT_START_MSG_MILLS) {
                return
            }

            val runTimeMills = SystemClock.uptimeMillis() - startMsgMills
            Log.e("rango-action", "action run, timeMills = $runTimeMills")
            if (runTimeMills > BLOCK_TIME_MILLS) {
                Log.e("rango-block",dumpStack())
            }
        }
    }

    private fun dumpStack(): String {
        val builder = StringBuilder()
        Thread.currentThread().stackTrace.forEach {
            builder.append(it.toString())
                .append("\n")
        }
        return builder.toString()
    }
}
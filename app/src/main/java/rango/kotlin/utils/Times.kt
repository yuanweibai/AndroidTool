package rango.kotlin.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Times {

    private const val DATE_HOUR_FORMAT = "HH:mm:ss"
    private const val DATE_YEAR_HOUR_FORMAT = "yyyy-MM-dd HH:mm:ss"

    @SuppressLint("SimpleDateFormat")
    fun millsToHMS(mills: Long): String {
        val totalSeconds = mills / 1000
        val seconds: Long = totalSeconds % 60
        val minutes = totalSeconds / 60
        val hours = seconds / 3600
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @SuppressLint("SimpleDateFormat")
    fun timeMillsToHMS(mills: Long): String {
        val dateFormat = SimpleDateFormat(DATE_HOUR_FORMAT)
        val date = Date()
        date.time = mills
        return dateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun timeMillsToYMD_HMS(mills: Long): String {
        val dateFormat = SimpleDateFormat(DATE_YEAR_HOUR_FORMAT)
        val date = Date()
        date.time = mills
        return dateFormat.format(date)
    }

    fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal = Calendar.getInstance()
        cal.timeInMillis = time1
        val v1Y = cal[Calendar.YEAR]
        val v1D = cal[Calendar.DAY_OF_YEAR]
        cal.timeInMillis = time2
        val v2Y = cal[Calendar.YEAR]
        val v2D = cal[Calendar.DAY_OF_YEAR]
        return v1Y == v2Y &&
                v1D == v2D
    }

}
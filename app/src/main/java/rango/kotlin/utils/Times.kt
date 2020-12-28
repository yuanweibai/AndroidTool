package rango.kotlin.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

object Times {

    private const val DATE_HOUR_FORMAT = "HH:mm:ss"

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

}
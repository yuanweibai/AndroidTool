package rango.kotlin.utils

import android.annotation.SuppressLint
import rango.tool.common.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

object Times {

    private const val DATE_HOUR_FORMAT = "HH:mm:ss"

    @SuppressLint("SimpleDateFormat")
    fun millsToHMS(mills: Long): String {
        val dateFormat = SimpleDateFormat(DATE_HOUR_FORMAT)
        val date = Date()
        date.time = mills
        return dateFormat.format(date)
    }
}
@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package rango.kotlin.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object Times {

    private const val DATE_HOUR_MS_FORMAT = "HH:mm:ss"
    private const val DATE_YEAR_HOUR_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val DATE_YEAR_MD_FORMAT = "yyyy-MM-dd"

    private val dateFormatHMS: SimpleDateFormat by lazy {
        SimpleDateFormat(DATE_HOUR_MS_FORMAT)
    }

    private val dateFormatYMD_HMS: SimpleDateFormat by lazy {
        SimpleDateFormat(DATE_YEAR_HOUR_FORMAT)
    }

    private val dateFormatYMD: SimpleDateFormat by lazy {
        SimpleDateFormat(DATE_YEAR_MD_FORMAT)
    }

    fun millsToHMS(mills: Long): String {
        val totalSeconds = mills / 1000
        val seconds: Long = totalSeconds % 60
        val minutes = totalSeconds / 60
        val hours = seconds / 3600
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    fun timeMillsToHMS(mills: Long): String {
        val date = Date()
        date.time = mills
        return dateFormatHMS.format(date)
    }


    fun timeMillsToYMD_HMS(mills: Long): String {
        val date = Date()
        date.time = mills
        return dateFormatYMD_HMS.format(date)
    }

    fun timeMillsToYMD(mills: Long): String {
        val date = Date()
        date.time = mills
        return dateFormatYMD.format(date)
    }

    fun strToMillsYMD_HMS(timeStr: String): Long? {
        val date = dateFormatYMD_HMS.parse(timeStr)
        return date?.time
    }

    fun strToMillsYMD(timeStr: String): Long? {
        val date = dateFormatYMD.parse(timeStr)
        return date?.time
    }

    fun strToMillsHMS(timeStr: String): Long? {
        val date = dateFormatHMS.parse(timeStr)
        return date?.time
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

    /**
     * @param currentMills 当前时间的毫秒数
     * @param comparedTimeHMS 比较的时间，只能是 HH:mm:ss 形式，如：21:00:00
     */
    fun getTimeDifferMills(currentMills: Long, comparedTimeHMS: String): Long {
        val currentYearMD = timeMillsToYMD(currentMills)
        val comparedMills = strToMillsYMD_HMS("$currentYearMD $comparedTimeHMS")
        return if (comparedMills == null) {
            0
        } else {
            comparedMills - currentMills
        }
    }

}
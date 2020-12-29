package rango.kotlin.walk

import android.os.SystemClock
import rango.kotlin.utils.Times
import rango.tool.common.utils.Preferences

/**
 * 当 targetSdkVersion == 29 时，需要动态申请权限：android.permission.ACTIVITY_RECOGNITION
 * 此权限时危险权限，且申请以后才能开始注册传感器，监听计步数。否则得重新注册。
 *
 * 计步是通过传感器 (Sensor.TYPE_STEP_COUNTER) 实现的，此传感器是原理如下：
 *
 * 1. 此传感器是硬件计步的，因此数据不会丢失，记录的是从上次激活传感器到现在的步数;
 * 2. 当手机关机或者重启后，都会从0开始计算步数
 * 3. 此传感器是需要激活的，猜测是第一次使用此传感器的时候就激活了
 *
 * 是否开关机，可以通过 SystemClock.elapsedRealtime() 来判断，此值代表从手机开机到现在的毫秒数。
 */
object StepManager {
    private const val PREF_TOTAL_STEP_OFFSET = "pref_total_step_offset"
    private const val PREF_TOTAL_STEP = "pref_total_step"
    private const val PREF_TODAY_STEP = "pref_today_step"
    private const val PREF_LAST_UPDATE_TIME = "pref_last_update_time"
    private const val PREF_TODAY_STEP_OFFSET = "pref_today_step_offset"
    private const val PREF_MACHINE_ELAPSED_REAL_TIME = "pref_machine_elapsed_real_time"


    private fun isRecordedTotalStepOffset(): Boolean {
        return getTotalStepOffset() != -1
    }

    private fun recordTotalStepOffset(offset: Int) {
        Preferences.getDefault().putInt(PREF_TOTAL_STEP_OFFSET, offset)
    }

    private fun getTotalStepOffset(): Int {
        return Preferences.getDefault().getInt(PREF_TOTAL_STEP_OFFSET, -1)
    }

    fun calculateStep(originalStepCount: Int) {
        recordTotalStep(originalStepCount)
        recordTodayStep(originalStepCount)
        recordElapsedRealTime()
    }

    private fun recordTotalStep(originalStepCount: Int) {
        if (isUsefulMachineReboot(originalStepCount)) {
            recordTotalStepOffset(-getTotalStep())
        }

        if (!isRecordedTotalStepOffset()) {
            recordTotalStepOffset(originalStepCount)
        }

        val totalStep = originalStepCount - getTotalStepOffset()
        Preferences.getDefault().putInt(PREF_TOTAL_STEP, totalStep)
    }

    fun getTotalStep(): Int {
        return Preferences.getDefault().getInt(PREF_TOTAL_STEP, 0)
    }

    private fun recordTodayStep(originalStepCount: Int) {
        val todayStep = if (Times.isSameDay(getLastUpdateTodayTime(), now())) {
            if (isUsefulMachineReboot(originalStepCount)) {
                recordTodayStepOffset(-getTodayStep())
            }

            originalStepCount - getTodayStepOffset()
        } else {
            recordTodayStepOffset(originalStepCount)
            0
        }
        Preferences.getDefault().putInt(PREF_TODAY_STEP, todayStep)
        recordLastUpdateTodayTime()
    }

    private fun recordTodayStepOffset(offset: Int) {
        Preferences.getDefault().putInt(PREF_TODAY_STEP_OFFSET, offset)
    }

    private fun getTodayStepOffset(): Int {
        return Preferences.getDefault().getInt(PREF_TODAY_STEP_OFFSET, 0)
    }

    fun getTodayStep(): Int {
        if (Times.isSameDay(getLastUpdateTodayTime(), now())) {
            return Preferences.getDefault().getInt(PREF_TODAY_STEP, 0)
        }
        return 0
    }

    private fun recordLastUpdateTodayTime() {
        Preferences.getDefault().putLong(PREF_LAST_UPDATE_TIME, now())
    }

    private fun getLastUpdateTodayTime(): Long {
        return Preferences.getDefault().getLong(PREF_LAST_UPDATE_TIME, 0)
    }

    private fun isUsefulMachineReboot(currentOriginalStepCount: Int): Boolean {
        if (!isRecordedTotalStepOffset()) {
            return false
        }
        if (SystemClock.elapsedRealtime() <= getLastElapsedRealTime()) {
            return true
        }

        if (currentOriginalStepCount < getTotalStepOffset()) {
            return true
        }

        return false
    }

    private fun recordElapsedRealTime() {
        Preferences.getDefault().putLong(PREF_MACHINE_ELAPSED_REAL_TIME, SystemClock.elapsedRealtime())
    }

    private fun getLastElapsedRealTime(): Long {
        return Preferences.getDefault().getLong(PREF_MACHINE_ELAPSED_REAL_TIME, 0)
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }
}
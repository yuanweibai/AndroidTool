package rango.kotlin.walk

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
 */
object StepManager {
    private const val PREF_STEP_OFFSET = "pref_step_offset"
    private const val PREF_TOTAL_STEP = "pref_total_step"
    private const val PREF_TODAY_STEP = "pref_today_step"
    private const val PREF_LAST_UPDATE_TIME = "pref_last_update_time"
    private const val PREF_TODAY_STEP_OFFSET = "pref_today_step_offset"


    private fun isRecordedStepOffset(): Boolean {
        return getStepOffset() != -1
    }

    private fun recordStepOffset(offset: Int) {
        Preferences.getDefault().putInt(PREF_STEP_OFFSET, offset)
    }

    private fun getStepOffset(): Int {
        return Preferences.getDefault().getInt(PREF_STEP_OFFSET, -1)
    }

    fun recordTotalStep(stepCount: Int) {
        if (!isRecordedStepOffset()) {
            recordStepOffset(stepCount)
        }

        val totalStep = stepCount - getStepOffset()
        Preferences.getDefault().putInt(PREF_TOTAL_STEP, totalStep)
    }

    fun getTotalStep():Int{
        return Preferences.getDefault().getInt(PREF_TOTAL_STEP,0)
    }

    fun recordTodayStep(stepCount: Int) {
        val todayStep: Int
        if (Times.isSameDay(getLastUpdateTodayTime(), now())) {
            todayStep = stepCount - Preferences.getDefault().getInt(PREF_TODAY_STEP_OFFSET, 0)
            Preferences.getDefault().putInt(PREF_TODAY_STEP, todayStep)
        } else {
            Preferences.getDefault().putInt(PREF_TODAY_STEP_OFFSET, stepCount)
            todayStep = 0
            Preferences.getDefault().putInt(PREF_TODAY_STEP, todayStep)
        }
        recordLastUpdateTodayTime()
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

    private fun now(): Long {
        return System.currentTimeMillis()
    }
}
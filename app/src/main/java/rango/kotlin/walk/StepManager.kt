package rango.kotlin.walk

import rango.kotlin.utils.Times
import rango.tool.common.utils.Preferences

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
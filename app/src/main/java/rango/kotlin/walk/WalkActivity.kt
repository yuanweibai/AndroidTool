package rango.kotlin.walk

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_walk_layout.*
import rango.kotlin.utils.Times
import rango.kotlin.wanandroid.common.utils.Threads
import rango.tool.androidtool.IStepCounter
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class WalkActivity : BaseActivity() {

    companion object {
        private const val UPDATE_INTERVAL_MILLS: Long = 2000

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, WalkActivity::class.java)
            context.startActivity(starter)
        }
    }

    private var iStepCounter: IStepCounter? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iStepCounter = IStepCounter.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            iStepCounter = null
        }
    }

    private var totalStep = 0
    private var todayStep = 0
    private var originalStep = 0

    private val updateStepAction = object : Runnable {
        override fun run() {
            val todayStepCount = StepManager.getTodayStepOuter()
            val totalStepCount = StepManager.getTotalStepOuter()

            Log.e("rango", "todayStepCount = $todayStepCount, totalStepCount = $totalStepCount")
            iStepCounter?.let {
                totalStep = it.totalStep
                todayStep = it.todayStep
                originalStep = it.originalStep
                updateMsgBuilder.insert(0, "\n")
                updateMsgBuilder.insert(0, "已更新：${Times.timeMillsToHMS(System.currentTimeMillis())}, \n--------todayStepCount = $todayStepCount, totalStepCount = $totalStepCount")
                refreshUi()
            }
            Threads.postMain(this, UPDATE_INTERVAL_MILLS)
        }
    }

    private var timeMills: Long = 0
    private val timeUpdateAction = object : Runnable {
        override fun run() {
            timeMills += 1000
            timeValueText.text = Times.millsToHMS(timeMills)
            Threads.postMain(this, 1000)
        }
    }

    private val updateMsgBuilder = StringBuilder("没有更新！！！")

    private fun refreshUi() {
        todayStepCountValueText.text = todayStep.toString()
        allStepCountValueText.text = totalStep.toString()
        updateMsgText.text = updateMsgBuilder.toString()
        originalStepCountValueText.text = originalStep.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_layout)

        updateMsgText.movementMethod = ScrollingMovementMethod.getInstance()
        timeValueText.text = Times.millsToHMS(0)
        refreshUi()

//        startRecordStepBtn.setOnClickListener {
//            StepService.start(this, serviceConnection)
//            Threads.postMain(updateStepAction, UPDATE_INTERVAL_MILLS)
//            Threads.postMain(timeUpdateAction, 1000)
//        }
        startRecordStepBtn.visibility = View.GONE

        StepService.start(this, serviceConnection)
        Threads.postMain(updateStepAction, UPDATE_INTERVAL_MILLS)
        Threads.postMain(timeUpdateAction, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        Threads.removeOnMainThread(updateStepAction)
        Threads.removeOnMainThread(timeUpdateAction)
    }
}
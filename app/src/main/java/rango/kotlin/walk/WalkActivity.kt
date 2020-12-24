package rango.kotlin.walk

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_walk_layout.*
import rango.kotlin.utils.Times
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class WalkActivity : BaseActivity(), SensorEventListener {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, WalkActivity::class.java)
            context.startActivity(starter)
        }
    }

    private val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val stepSensor: Sensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_layout)

        timeValueText.text = Times.millsToHMS(0)
        todayStepCountValueText.text = "0"
        allStepCountValueText.text = "0"
        updateMsgText.text = "没有更新!!!"

        startRecordStepBtn.setOnClickListener {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.run {
            if (sensor.type != Sensor.TYPE_STEP_COUNTER) {
                return
            }

//            stepCountText.text = values[0].toString()

            Log.e("rango", "step count = ${values[0]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
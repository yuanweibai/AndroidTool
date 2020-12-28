package rango.kotlin.walk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import rango.tool.androidtool.IStepCounter
import rango.tool.androidtool.R
import rango.tool.androidtool.ToolApplication

class StepService : Service(), SensorEventListener {

    companion object {
        private const val NOTIFICATION_ID = 9001

        @JvmStatic
        fun start(context: Context, serviceConnection: ServiceConnection) {
            val starter = Intent(context, StepService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(starter)
            } else {
                context.startService(starter)
            }

            context.bindService(starter, serviceConnection, BIND_AUTO_CREATE)
        }
    }

    private val binder = object : IStepCounter.Stub() {
        override fun getTodayStep(): Int {
            return 109
        }

        override fun getTotalStep(): Int {
            return 10009
        }
    }


    private val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val stepSensor: Sensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onCreate() {
        super.onCreate()
        showNotification()

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.run {
            if (sensor.type != Sensor.TYPE_STEP_COUNTER) {
                return
            }

            Log.e("rango", "step count = ${values[0]}")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private fun showNotification() {
        val notificationManager = ToolApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "9002"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "step_counter", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在记录步数")
                .setContentText("步数：--")
                .setPriority(NotificationCompat.PRIORITY_MAX)
        val intent = Intent(this, WalkActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        startForeground(NOTIFICATION_ID, builder.build())
    }
}
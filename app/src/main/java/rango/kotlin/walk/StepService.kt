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
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import rango.kotlin.utils.Files
import rango.kotlin.utils.Times
import rango.tool.androidtool.IStepCounter
import rango.tool.androidtool.R
import rango.tool.androidtool.ToolApplication

class StepService : Service(), SensorEventListener {

    companion object {
        private const val NOTIFICATION_ID = 9001
        private val NOTIFICATION_CHANNEL_ID = ToolApplication.getContext().packageName+".step_channel_id"
        private val NOTIFICATION_CHANNEL_NAME = ToolApplication.getContext().packageName+".step_channel_name"
        private const val STEP_LOG_MSG_FILE_NAME = "step_log_msg.txt"

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

    private var originalStepCount = 0

    private val binder = object : IStepCounter.Stub() {
        override fun getTodayStep(): Int {
            return StepManager.getTodayStepInner()
        }

        override fun getTotalStep(): Int {
            return StepManager.getTotalStepInner()
        }

        override fun getOriginalStep(): Int {
            return originalStepCount
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

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
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

            originalStepCount = values[0].toInt()
            StepManager.calculateStep(originalStepCount)
            updateNotification()
            addLogMsg()
        }
    }

    private fun addLogMsg() {
        val msg = "${Times.timeMillsToYMD_HMS(System.currentTimeMillis())}    todayStepCounter=${StepManager.getTodayStepInner()}     totalStepCounter=${StepManager.getTotalStepInner()}    originalStepCounter=${originalStepCount}"
        Files.appendMsg(STEP_LOG_MSG_FILE_NAME, msg)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        val builder: NotificationCompat.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN)
            notificationManager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        } else {
            NotificationCompat.Builder(this)
                    .setSound(null)
                    .setVibrate(longArrayOf(0))
                    .setPriority(NotificationCompat.PRIORITY_MIN)
        }
        builder
    }
    private val notificationManager: NotificationManager by lazy {
        ToolApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun showNotification() {
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("正在记录步数")
                .setContentText("步数：0")

        val intent = Intent(this, WalkActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun updateNotification() {
        notificationBuilder.setContentText("今日步数：${StepManager.getTodayStepInner()}")
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}
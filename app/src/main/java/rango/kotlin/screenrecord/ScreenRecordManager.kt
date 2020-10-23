package rango.kotlin.screenrecord

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.screen_record_layout.view.*
import rango.tool.androidtool.R
import rango.tool.androidtool.ToolApplication
import rango.tool.common.utils.Permissions
import rango.tool.common.utils.ScreenUtils
import java.io.File
import java.lang.Exception

object ScreenRecordManager {

    const val TAG = "ScreenRecordManager";

    private val windowManager: WindowManager = ToolApplication.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var screenView: View? = null

    private val projectionManager: MediaProjectionManager = ToolApplication.getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var recordFilePath: String = ""
    private var mediaRecorder: MediaRecorder? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val screenWidth = ScreenUtils.getScreenWidthPx()
    private val screenHeight = ScreenUtils.getScreenHeightPx()


    private fun checkPermission(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) + ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 145)
                return false
            }
        }
        return true
    }

    fun setupData(resultCode: Int, resultData: Intent) {
        if (mediaProjection != null) {
            return
        }
        mediaProjection = projectionManager.getMediaProjection(resultCode, resultData)
        start()
    }

    @SuppressLint("InflateParams")
    fun showScreenRecordView(activity: Activity, requestCode: Int) {
        if (!Permissions.isFloatWindowAllowed(ToolApplication.getContext())) {
            Permissions.requestFloatWindowPermission(ToolApplication.getContext())
            return
        }
        if (screenView != null) {
            removeScreenView()
        }
        screenView = LayoutInflater.from(ToolApplication.getContext()).inflate(R.layout.screen_record_layout, null)

        screenView!!.startBtn.setOnClickListener {
            if (checkPermission(activity)) {
                startScreenRecord(activity, requestCode)
            }

        }
        screenView!!.stopBtn.setOnClickListener {
            stop()
        }
        windowManager.addView(screenView, getLayoutParams())
    }

    private fun startScreenRecord(activity: Activity, requestCode: Int) {
        val intent = projectionManager.createScreenCaptureIntent()
        val packageManager = activity.packageManager
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            Toast.makeText(activity, "无法录制", Toast.LENGTH_LONG).show()
        }
    }

    private fun start() {
        screenView?.msgText?.text = "屏幕录制中......"

        setupMediaRecorder()
        createVirtualDisplay()
        mediaRecorder?.start()
    }

    private fun stop() {
        screenView?.msgText?.text = "屏幕录制已停止"
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            virtualDisplay?.release()
            mediaProjection?.stop()
            mediaProjection = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeScreenView() {
        windowManager.removeView(screenView)
        screenView = null
    }

    private fun getLayoutParams(): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        }

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = ScreenUtils.dp2px(100f)
        layoutParams.format = PixelFormat.TRANSPARENT
        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        layoutParams.gravity = Gravity.TOP
        layoutParams.flags = (WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        return layoutParams
    }

    private fun setupMediaRecorder() {
        recordFilePath = getSaveDirectory() + File.separator + System.currentTimeMillis() + ".mp4"
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setOutputFile(recordFilePath)
        mediaRecorder?.setVideoSize(screenWidth, screenHeight)
        mediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setVideoEncodingBitRate((screenWidth * screenHeight * 3.6f).toInt())
        mediaRecorder?.setVideoFrameRate(20)

        try {
            mediaRecorder?.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createVirtualDisplay() {
        virtualDisplay = mediaProjection?.createVirtualDisplay("MainScreen", screenWidth, screenHeight, ScreenUtils.getDensityDpi(),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder?.surface, null, null)
    }

    private fun getSaveDirectory(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }


}
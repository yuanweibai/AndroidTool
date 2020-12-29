package rango.kotlin.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

object Permissions {

    private const val PERMISSION_REQUEST_CODE = 1


    private val STORAGE_PERMISSION = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    @RequiresApi(Build.VERSION_CODES.Q)
    private val STEP_COUNTER_PERMISSION = arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)

    fun requestStoragePermission(activity: Activity) {
        try {
            if (!checkPermissions(activity, STORAGE_PERMISSION)) {
                ActivityCompat.requestPermissions(activity, STORAGE_PERMISSION, PERMISSION_REQUEST_CODE)
            }
        } catch (ignore: Exception) {
        }
    }

    private fun checkPermissions(activity: Activity, permissionArray: Array<String>): Boolean {
        val results = arrayOfNulls<Int>(permissionArray.size)
        for (i in permissionArray.indices) {
            results[i] = ActivityCompat.checkSelfPermission(activity, permissionArray[i])
        }

        results.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestStepCounterPermission(activity: Activity) {
        try {
            if (!checkPermissions(activity, STEP_COUNTER_PERMISSION)) {
                ActivityCompat.requestPermissions(activity, STEP_COUNTER_PERMISSION, PERMISSION_REQUEST_CODE)
            }
        } catch (ignore: Exception) {
            ignore.printStackTrace()
        }
    }
}
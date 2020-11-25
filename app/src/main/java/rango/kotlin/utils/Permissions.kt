package rango.kotlin.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object Permissions {

    private const val PERMISSION_REQUEST_CODE = 1


    private val STORAGE_PERMISSION = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    fun requestStoragePermission(activity: Activity) {
        try {
            if (!checkPermissions(activity, STORAGE_PERMISSION)) {
                ActivityCompat.requestPermissions(activity, STORAGE_PERMISSION, PERMISSION_REQUEST_CODE)
            }
        } catch (ignore: Exception) {
        }
    }

    private fun checkPermissions(activity: Activity, permissionArray: Array<String>): Boolean {
        val resultList: MutableList<Int> = ArrayList(permissionArray.size)
        for (i in permissionArray.indices) {
            resultList[i] = ActivityCompat.checkSelfPermission(activity, permissionArray[i])
        }

        resultList.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
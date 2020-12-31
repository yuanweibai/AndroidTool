package rango.kotlin.walk

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import rango.tool.androidtool.ToolApplication

class StepProvider : ContentProvider() {


    companion object {
        private val AUTHORITIES = ToolApplication.getContext().packageName + ".StepProvider.authorities"

        const val METHOD_GET_TODAY_STEP_COUNT = "method_get_today_step_count"
        const val METHOD_GET_TOTAL_STEP_COUNT = "method_get_total_step_count"

        const val VALUE_KEY_TODAY_STEP_COUNT = "value_key_today_step_count"
        const val VALUE_KEY_TOTAL_STEP_COUNT = "value_key_total_step_count"

        fun createContentUri(): Uri {
            return Uri.parse("content://$AUTHORITIES")
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle {
        val bundle = Bundle()
        when (method) {
            METHOD_GET_TODAY_STEP_COUNT -> {
                bundle.putInt(VALUE_KEY_TODAY_STEP_COUNT, StepManager.getTodayStepInner())
            }
            METHOD_GET_TOTAL_STEP_COUNT -> {
                bundle.putInt(VALUE_KEY_TOTAL_STEP_COUNT, StepManager.getTotalStepInner())
            }
        }
        return bundle
    }
}
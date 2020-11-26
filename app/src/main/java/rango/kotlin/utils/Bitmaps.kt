package rango.kotlin.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import rango.tool.androidtool.ToolApplication

object Bitmaps {

    fun getScaledBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val matrix = Matrix()
        val scaleW = width.toFloat() / w
        val scaleH = height.toFloat() / h
        matrix.postScale(scaleW, scaleH)
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

    fun getScaledBitmap(resId: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = 4
        options.inPreferredConfig = Bitmap.Config.RGB_565
        return BitmapFactory.decodeResource(ToolApplication.getContext().resources, resId, options)
    }
}
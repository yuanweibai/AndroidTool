package rango.kotlin.utils

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import rango.tool.androidtool.ToolApplication
import java.io.*
import java.lang.Exception

object FileUtils {

    private val FILE_DIR = Environment.getExternalStorageDirectory().absolutePath + "/AndroidTool/"
    fun saveBitmap(bitmap: Bitmap, fileName: String, format: Bitmap.CompressFormat) {

        val dir = File(FILE_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val bitmapFile = File(FILE_DIR + fileName)
        if (!bitmapFile.exists()) {
            bitmapFile.createNewFile()
        }

        val bos = BufferedOutputStream(FileOutputStream(bitmapFile))
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, bos)
        bos.flush()
        bos.close()
    }

    /**
     * base64 应该去掉头部的【data:image/png;base64,】
     */
    fun readImageBase64FromAssets(): String {
        try {
            val inputStream = ToolApplication.getContext().getAssets().open("imageBase64.txt");
            val reader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(reader)
            return bufferedReader.readLine()
        } catch (e: Exception) {
        }
        return ""
    }
}
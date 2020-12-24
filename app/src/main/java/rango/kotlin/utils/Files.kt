package rango.kotlin.utils

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import rango.tool.androidtool.ToolApplication
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

object Files {

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

    fun readJsonStr(): String {
        return readAssetsFile("joke_data.json")
    }

    fun readFilters(): List<String> {
        val data = readAssetsFile("filters.txt")
        return data.split("、")
    }

    private fun readAssetsFile(fileName: String): String {
        return try {
            val inputStream = ToolApplication.getContext().assets.open(fileName)
            val reader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(reader)
            var line = bufferedReader.readLine()
            val builder = StringBuilder()
            while (line != null) {
                builder.append(line)
                line = bufferedReader.readLine()
            }
            builder.toString()
        } catch (e: Exception) {
            ""
        }
    }
}
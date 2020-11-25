package rango.kotlin.utils

import android.graphics.Bitmap
import android.os.Environment
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

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
}
package rango.kotlin.apk

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class DownloadCompeteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE != action) {
            return
        }

        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val query = DownloadManager.Query()
        val dm = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        query.setFilterById(id)
        val cursor = dm.query(query)
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    val file = File(context.getExternalFilesDir("download"), "popularize.apk");
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            val apkUri = FileProvider.getUriForFile(context, context.packageName + ".popularize.provider", file)
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                        } else {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
                        }

                        context.startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
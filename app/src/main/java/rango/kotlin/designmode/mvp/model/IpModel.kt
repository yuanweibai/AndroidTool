package rango.kotlin.designmode.mvp.model

import rango.kotlin.designmode.mvp.presenter.IResultListener
import rango.tool.common.utils.Worker
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class IpModel {
    fun getIpInfo(ip: String, resultListener: IResultListener) {
        Thread(Runnable {
            val apiUrl = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=Json&ip="
            try {
                val url = URL(apiUrl + ip)
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                val isp = InputStreamReader(urlConnection.inputStream)
                val reader = BufferedReader(isp)
                var line: String? = reader.readLine()
                var stringBuilder = StringBuilder()

                while (line != null) {
                    stringBuilder.append(line).append("\n")
                    line = reader.readLine()
                }

                val msg = stringBuilder.toString()
                Worker.postMain {
                    resultListener.onSuccess(msg)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                val msg = e.message
                Worker.postMain {
                    resultListener.onFailure(msg)
                }

            }
        }).start()
    }
}
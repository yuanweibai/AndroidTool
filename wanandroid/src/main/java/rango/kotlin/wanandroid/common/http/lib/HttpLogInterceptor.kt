package rango.kotlin.wanandroid.common.http.lib

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Logger
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class HttpLogInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT, private val level: Level = Level.BASIC) : Interceptor {

    companion object {
        const val HEADER_KEY_LOG_LEVEL = "LogLevel"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val stringBuilder = StringBuilder()
        val logInterceptor = HttpLoggingInterceptor(object : Logger {
            override fun log(message: String) {
                appendMsg(stringBuilder, message)
            }

            private fun appendMsg(builder: StringBuilder, msg: String) {
                if (TextUtils.isEmpty(msg)) {
                    return
                }
                var temp = msg
                try {
                    if (msg.startsWith("{") && msg.endsWith("}")) {
                        val jsonObject = JSONObject(msg)
                        temp = jsonObject.toString(2)
                    } else if (msg.startsWith("[") && msg.endsWith("]")) {
                        val array = JSONArray(msg)
                        temp = array.toString(2)
                    }
                } catch (e: Exception) {
                }
                builder.append(temp).append('\n')
            }
        })

        logInterceptor.level = findLevel(chain.request())
        val response = logInterceptor.intercept(chain)
        logger.log(stringBuilder.toString())
        return response
    }

    private fun findLevel(request: Request): Level {
        val msgLevel = request.header(HEADER_KEY_LOG_LEVEL)
        if (TextUtils.isEmpty(msgLevel)) {
            return level
        }
        when (msgLevel!!.toUpperCase(Locale.ROOT)) {
            "NONE" -> {
                return Level.NONE
            }
            "BASIC" -> {
                return Level.BASIC
            }
            "HEADERS" -> {
                return Level.HEADERS
            }
            "BODY" -> {
                return Level.BODY
            }
        }
        return level
    }
}
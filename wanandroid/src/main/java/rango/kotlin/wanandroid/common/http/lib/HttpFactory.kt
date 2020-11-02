package rango.kotlin.wanandroid.common.http.lib

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rango.kotlin.wanandroid.BuildConfig
import rango.kotlin.wanandroid.common.http.api.IHttpRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object HttpFactory {

    const val HTTP_LOG_TAG = "HttpLog"

    fun getDefaultRetrofit(): Retrofit {
        initHttpLog()

        val logInterceptor = HttpLogInterceptor(HttpLoggingInterceptor.Logger { message -> Logger.d(message) }, HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(logInterceptor)
                .build()
        return Retrofit.Builder()
                .baseUrl(IHttpRequest.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    private fun initHttpLog() {
        Logger.addLogAdapter(object : AndroidLogAdapter(PrettyFormatStrategy
                .newBuilder()
                .tag(HTTP_LOG_TAG)
                .methodCount(1)
                .showThreadInfo(true)
                .build()) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}
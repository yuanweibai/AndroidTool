package rango.kotlin.wanandroid.common.http.api

import rango.kotlin.wanandroid.common.http.api.bean.LoginBean
import rango.kotlin.wanandroid.common.http.lib.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IHttpRequest {
    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") userName: String, @Field("password") password: String): Response<LoginBean>

}
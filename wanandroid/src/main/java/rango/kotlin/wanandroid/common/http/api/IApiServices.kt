package rango.kotlin.wanandroid.common.http.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IApiServices {
    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @FormUrlEncoded
    @POST
    fun login(
            @Field("username") userName: String,
            @Field("password") password: String
    )


}
package rango.tool.androidtool.http;

import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IHttpRequest {

    String BASE_URL_NAME = "BaseUrlName";
    String NAME_WAN_ANDROID = "wan_android";

    @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
    @FormUrlEncoded
    Call<TranslationBean> translation(@Field("i") String targetSentence);

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    Call<TranslationGetBean> translationGet();

    @FormUrlEncoded
    @Headers(BASE_URL_NAME + ":" + NAME_WAN_ANDROID)
    @POST("user/login")
    Call<LoginInfoBean> login(@Field("username") String username, @Field("password") String password);

    @Streaming
    @Headers("LogLevel:BASIC")
    @GET
    DownloadFileCall downloadFile(@Url String url);

}


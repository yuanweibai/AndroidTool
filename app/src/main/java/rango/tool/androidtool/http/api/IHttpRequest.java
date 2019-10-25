package rango.tool.androidtool.http.api;


import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rango.tool.androidtool.http.api.bean.AllThemeBean;
import rango.tool.androidtool.http.api.bean.AllUserThemeBean;
import rango.tool.androidtool.http.api.bean.LoginUserBean;
import rango.tool.androidtool.http.lib.call.Callable;
import rango.tool.androidtool.http.lib.download.DownloadFileCall;
import rango.tool.androidtool.http.lib.upload.UploadMoreFiles;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IHttpRequest {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @HTTP(method = "POST", path = "user/login", hasBody = true)
    Callable<LoginUserBean> login(@Body RequestBody body);

    @PUT("user/{uid}/profile")
    Callable<ResponseBody> editUserInfo(@Header("X-ColorPhone-Session-Token") String token, @Path("uid") String uid, @Body RequestBody body);

    @GET("user/{uid}/profile")
    Callable<LoginUserBean> getUserInfo(@Header("X-ColorPhone-Session-Token") String token, @Path("uid") String uid);

    @GET("shows")
    Callable<AllThemeBean> getAllThemes(@Query("per_page") int perPage, @Query("page_index") int pageIndex);

    @UploadMoreFiles
    @Headers("LogLevel:BASIC")
    @POST("user/{uid}/uploaded_shows")
    Callable<ResponseBody> uploadVideos(@Header("X-ColorPhone-Session-Token") String token, @Path("uid") String uid, @Body HashMap<String, Object> params);

    @GET("user/{uid}/uploaded_shows")
    Callable<AllUserThemeBean> getUserUploadedVideos(@Header("X-ColorPhone-Session-Token") String token, @Path("uid") String uid, @Query("per_page") int perPage, @Query("page_index") int pageIndex);

    @GET("user/{uid}/reviewed_shows")
    Callable<AllUserThemeBean> getUserPublishedVideos(@Header("X-ColorPhone-Session-Token") String token, @Path("uid") String uid, @Query("per_page") int perPage, @Query("page_index") int pageIndex);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @HTTP(method = "DELETE", path = "user/{uid}/uploaded_shows", hasBody = true)
    Callable<ResponseBody> deleteUserVideos(@Header("X-ColorPhone-Session-Token") String token, @Path("uid") String uid, @Body RequestBody body);

    @Streaming
    @Headers("LogLevel:BASIC")
    @GET
    DownloadFileCall downloadFile(@Url String url);
}


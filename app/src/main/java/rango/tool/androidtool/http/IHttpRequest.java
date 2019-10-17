package rango.tool.androidtool.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import rango.tool.androidtool.http.download.DownloadFileCall;
import rango.tool.androidtool.http.upload.UploadMoreFiles;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    /**
     * 单个文件上传
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadSingleFile(@Part MultipartBody.Part file);

    /**
     * 单个文件与参数混合上传
     * 使用 ProgressRequest
     */
    @POST("upload")
    Call<ResponseBody> uploadFileAndDescription(@Body RequestBody body);

    /**
     * 多文件与参数上传
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadMoreFile(@PartMap Map<String, RequestBody> map);

    /**
     * 多文件与参数上传
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadMoreFilePart(@Part("user_name") RequestBody userName, @Part("gender") RequestBody gender, @Part List<MultipartBody.Part> partList);

    /**
     * 多文件与参数上传，并监听进度
     */
    @UploadMoreFiles
    @POST("upload")
    Call<ResponseBody> uploadMoreFileWithProgress(@Body HashMap<String, Object> params);
}


package rango.tool.androidtool.http;

import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import rango.tool.androidtool.BuildConfig;
import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import rango.tool.androidtool.http.download.DownloadFileCall;
import rango.tool.androidtool.http.download.DownloadFileCallAdapterFactory;
import rango.tool.androidtool.http.download.DownloadFileCallback;
import rango.tool.androidtool.http.upload.FilesConvertFactory;
import rango.tool.androidtool.http.upload.FilesRequestBodyConverter;
import rango.tool.androidtool.http.upload.ProgressRequestBody;
import rango.tool.androidtool.http.upload.UploadFileCallback;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class HttpManager {

    private static final String HTTP_LOG_TAG = "HttpLog";

    private final Retrofit DEFAULT;

    private HttpManager() {

        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy
                .newBuilder()
                .tag(HTTP_LOG_TAG)
                .methodCount(1)
                .showThreadInfo(true)
                .build()) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });

        FullLoggingInterceptor loggingInterceptor = new FullLoggingInterceptor(Logger::d);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        DEFAULT = new Retrofit.Builder()
                .baseUrl(HttpConstant.BASE_URL)
                .addCallAdapterFactory(CallAdapterFactory.getInstance())
                .addCallAdapterFactory(DownloadFileCallAdapterFactory.getInstance())
                .addConverterFactory(new FilesConvertFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .callFactory(new ReplaceUrlCallFactory(client))
                .build();
    }

    private static class ClassHolder {
        private final static HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getInstance() {
        return ClassHolder.INSTANCE;
    }

    public void translate(String params, Callback<TranslationBean> callback) {
        DEFAULT.create(IHttpRequest.class)
                .translation(params)
                .enqueue(callback);
    }

    public void translateGet(retrofit2.Callback<TranslationGetBean> callback) {
        DEFAULT.create(IHttpRequest.class)
                .translationGet()
                .enqueue(callback);
    }

    public void login(String userName, String password, Callback<LoginInfoBean> callback) {
        DEFAULT.create(IHttpRequest.class)
                .login(userName, password)
                .enqueue(callback);
    }

    public CancelableCall downloadFile(String url, DownloadFileCallback callback) {
        DownloadFileCall call = DEFAULT.create(IHttpRequest.class)
                .downloadFile(url);
        call.enqueue(callback);
        return call;
    }

    public void uploadSingleFile(String filePath, retrofit2.Callback<ResponseBody> callback) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }


        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), fileBody);

        DEFAULT.create(IHttpRequest.class)
                .uploadSingleFile(part)
                .enqueue(callback);
    }

    public void uploadFileAndDescription(String userName, String gender, String birthday, String filePath, retrofit2.Callback<ResponseBody> callback) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("userName", userName)
                .addFormDataPart("gender", gender)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        DEFAULT.create(IHttpRequest.class)
                .uploadFileAndDescription(body)
                .enqueue(callback);
    }

    public void uploadMoreFile(List<String> filePathList, String userName, String gender, retrofit2.Callback<ResponseBody> callback) {
        if (filePathList == null || filePathList.isEmpty()) {
            return;
        }
        Map<String, RequestBody> map = new HashMap<>();

        for (String filePath : filePathList) {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                continue;
            }
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            map.put("file\";filename=\"" + file.getName(), fileBody);
        }

        map.put("userName", RequestBody.create(MediaType.parse("text/plain"), userName));
        map.put("gender", RequestBody.create(MediaType.parse("text/plain"), gender));

        DEFAULT.create(IHttpRequest.class)
                .uploadMoreFile(map)
                .enqueue(callback);
    }

    public void uploadMoreFileByPart(List<String> filePathList, String userName, String gender, retrofit2.Callback<ResponseBody> callback) {

        List<MultipartBody.Part> partList = new ArrayList<>();

        for (String filePath : filePathList) {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                continue;
            }

            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/for-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
            partList.add(part);
        }

        RequestBody userBody = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody genderBody = RequestBody.create(MediaType.parse("text/plain"), gender);

        DEFAULT.create(IHttpRequest.class)
                .uploadMoreFilePart(userBody, genderBody, partList)
                .enqueue(callback);
    }

    /**
     * 上传单个文件，并监听进度
     *
     * @param filePath
     */
    public void uploadFileWithProgress(String filePath, String userName, String gender, UploadFileCallback callback) {

        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        ProgressRequestBody fileBody = new ProgressRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), file)) {
            @Override
            public void onUpload(long length, long current, boolean isDone) {
                if (callback != null) {
                    callback.onUpload(length, current, isDone);
                }
            }
        };

        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("userName", userName)
                .addFormDataPart("gender", gender)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        DEFAULT.create(IHttpRequest.class)
                .uploadFileAndDescription(body)
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (callback != null) {
                                callback.onSuccess();
                            }
                        } else {
                            if (callback != null) {
                                callback.onFailure(response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (callback != null) {
                            callback.onFailure(t.getMessage());
                        }
                    }
                });
    }

    /**
     * 上传多个文件及参数，并监听进度
     */
    public void uploadMoreFilesWithProgress(List<String> filePathList, String userName, String gender, UploadFileCallback callback) {
        if (filePathList == null || filePathList.isEmpty()) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(FilesRequestBodyConverter.KEY_FILE_PATH_LIST, filePathList);
        map.put(FilesRequestBodyConverter.KEY_UPLOAD_FILE_CALLBACK, callback);
        map.put("userName", userName);
        map.put("gender", gender);

        DEFAULT.create(IHttpRequest.class)
                .uploadMoreFileWithProgress(map)
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            success();
                        } else {
                            failure(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        failure(t.getMessage());
                    }

                    private void success() {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }

                    private void failure(String errorMsg) {
                        if (callback != null) {
                            callback.onFailure(errorMsg);
                        }
                    }
                });
    }
}

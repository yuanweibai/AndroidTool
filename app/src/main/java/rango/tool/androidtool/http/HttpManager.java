package rango.tool.androidtool.http;

import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import rango.tool.androidtool.BuildConfig;
import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import retrofit2.Call;
import retrofit2.Callback;
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

    public void translateGet(Callback<TranslationGetBean> callback) {
        DEFAULT.create(IHttpRequest.class)
                .translationGet()
                .enqueue(callback);
    }

    public void login(String userName, String password, Callback<LoginInfoBean> callback) {
        DEFAULT.create(IHttpRequest.class)
                .login(userName, password)
                .enqueue(callback);
    }

    public void downloadFile(String url, String filePath, DownloadCallback callback) {
        DEFAULT.create(IHttpRequest.class)
                .downloadFile(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (writeBodyToFile(response.body(), filePath)) {
                                if (callback != null) {
                                    callback.onSuccess();
                                }
                            } else {
                                failure();
                            }
                        } else {
                            failure();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        failure();
                    }

                    private void failure() {
                        if (callback != null) {
                            callback.onFailure();
                        }
                    }
                });
    }

    private boolean writeBodyToFile(ResponseBody value, String path) {
        File file = new File(path);
        if (file.exists() && !file.delete()) {
            return false;
        }

        File tmp = new File(file.getPath() + ".tmp");

        if (tmp.exists() && !tmp.delete()) {
            return false;
        }

        if (value == null) {
            return false;
        }

        InputStream is = value.byteStream();
        FileOutputStream fos = null;
        try {
            if (!tmp.createNewFile()) {
                return false;
            }

            fos = new FileOutputStream(tmp);
            byte[] buffer = new byte[8096];
            int c;
            while ((c = is.read(buffer)) != -1) {
                fos.write(buffer, 0, c);
            }
            return tmp.renameTo(file);
        } catch (IOException e) {
            return false;
        } finally {
            Util.closeQuietly(fos);
        }
    }

}

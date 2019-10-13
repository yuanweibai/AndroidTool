package rango.tool.androidtool.http;

import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rango.tool.androidtool.BuildConfig;
import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import retrofit2.Callback;
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
                .addCallAdapterFactory(DownloadFileCallAdapterFactory.getInstance())
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

    public CancelableCall downloadFile(String url, DownloadFileCallback callback) {
        DownloadFileCall call = DEFAULT.create(IHttpRequest.class)
                .downloadFile(url);
        call.enqueue(callback);
        return call;
    }
}

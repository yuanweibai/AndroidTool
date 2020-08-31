package rango.tool.androidtool.http.lib.utils;

import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rango.tool.androidtool.BuildConfig;
import rango.tool.androidtool.http.api.IHttpConstant;
import rango.tool.androidtool.http.lib.call.CallAdapterFactory;
import rango.tool.androidtool.http.lib.download.DownloadFileCallAdapterFactory;
import rango.tool.androidtool.http.lib.log.FullLoggingInterceptor;
import rango.tool.androidtool.http.lib.signature.SignatureInterceptor;
import rango.tool.androidtool.http.lib.upload.FilesConvertFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitFactory {

    private static final String HTTP_LOG_TAG = "HttpLog";

    public static Retrofit getDefault() {
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
                .addInterceptor(new SignatureInterceptor())
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(IHttpConstant.BASE_URL)
                .addCallAdapterFactory(CallAdapterFactory.getInstance())
                .addCallAdapterFactory(DownloadFileCallAdapterFactory.getInstance())
                .addConverterFactory(new FilesConvertFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

}

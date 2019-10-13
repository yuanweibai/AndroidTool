package rango.tool.androidtool.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public final class DownloadFileCallAdapterFactory extends CallAdapter.Factory {

    public static DownloadFileCallAdapterFactory getInstance() {
        return ClassHolder.INSTANCE;
    }

    private static class ClassHolder {
        private final static DownloadFileCallAdapterFactory INSTANCE = new DownloadFileCallAdapterFactory();
    }
    private DownloadFileCallAdapterFactory() {
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        if (returnType != DownloadFileCall.class) {
            return null;
        }
        return new CallAdapter<ResponseBody, DownloadFileCall>() {
            @Override
            public Type responseType() {
                return ResponseBody.class;
            }

            @Override
            public DownloadFileCall adapt(Call<ResponseBody> call) {
                return new DownloadFileCall(call);
            }
        };

    }
}

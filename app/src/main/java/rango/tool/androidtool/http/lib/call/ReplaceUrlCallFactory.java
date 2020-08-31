package rango.tool.androidtool.http.lib.call;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import rango.tool.androidtool.http.api.IHttpConstant;

public class ReplaceUrlCallFactory implements Call.Factory {

    private final Call.Factory delegate;

    public ReplaceUrlCallFactory(@NonNull Call.Factory delegate) {
        this.delegate = delegate;
    }

    @Override
    public final Call newCall(Request request) {
        /*
         * @Headers("BaseUrlName:xxx") for method, or
         * method(@Header("BaseUrlName") String name) for parameter
         */
        String baseUrlName = request.header(IHttpConstant.BASE_URL_NAME);
        if (baseUrlName != null) {
            okhttp3.HttpUrl newHttpUrl = getNewUrl(baseUrlName, request);
            if (newHttpUrl != null) {
                Request newRequest = request.newBuilder().url(newHttpUrl).build();
                return delegate.newCall(newRequest);
            } else {
                throw new IllegalStateException("NewHttpUrl is null!!!");
            }
        }
        return delegate.newCall(request);
    }

    /**
     * @param baseUrlName name to sign baseUrl
     * @return new httpUrl, if null use old httpUrl
     */
    @Nullable
    protected HttpUrl getNewUrl(String baseUrlName, Request request) {
        switch (baseUrlName) {
            case IHttpConstant.NAME_WAN_ANDROID:
                String oldUrl = request.url().toString();
                String newUrl = oldUrl.replace(IHttpConstant.BASE_URL,IHttpConstant.BASE_URL_WAN_ANDROID);
                return HttpUrl.get(newUrl);
            default:
                return HttpUrl.get(IHttpConstant.BASE_URL);
        }
    }
}

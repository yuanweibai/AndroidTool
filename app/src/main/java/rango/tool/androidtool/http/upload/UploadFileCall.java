package rango.tool.androidtool.http.upload;

import okhttp3.RequestBody;
import rango.tool.androidtool.http.IFileCall;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFileCall implements IFileCall {

    private final Call<RequestBody> delegate;

    public UploadFileCall(Call<RequestBody> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void enqueue(Object callback) {
        enqueue(DEFAULT_INCREASE, callback);
    }

    @Override
    public void enqueue(float increaseOfProgress, Object callback) {

        if (!(callback instanceof UploadFileCallback)) {
            return;
        }

        UploadFileCallback uploadFileCallback = (UploadFileCallback) callback;

        delegate.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {

            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {

            }
        });


    }

    @Override
    public void cancel() {

    }
}

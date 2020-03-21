package rango.tool.androidtool.http.lib.call;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

public final class RealCall<T> implements Callable<T> {

    private final Executor callBackExecutor;
    private final Call<T> delegate;

    RealCall(Executor executor, Call<T> delegate) {
        callBackExecutor = executor;
        this.delegate = delegate;
    }

    @NonNull @Override public T execute() throws Throwable {
        Response<T> response = delegate.execute();
        T body = response.body();
        if (body != null) {
            return body;
        }
        throw new HttpException(response);
    }

    @Override
    public void enqueue(Callback<T> callback) {
        delegate.enqueue(new retrofit2.Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response.isSuccessful()) {
                    success(response.body());
                } else {
                    failure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                failure(t.getMessage());
            }

            private void success(T t) {
                callBackExecutor.execute(new Runnable() {
                    @Override public void run() {
                        if (callback != null) {
                            callback.onSuccess(t);
                        }
                    }
                });
            }

            private void failure(String errorMsg) {
                callBackExecutor.execute(new Runnable() {
                    @Override public void run() {
                        if (callback != null) {
                            callback.onFailure(errorMsg);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }
}

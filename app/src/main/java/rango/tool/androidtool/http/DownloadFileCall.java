package rango.tool.androidtool.http;

import android.util.Log;

import java.io.File;

import okhttp3.ResponseBody;
import rango.tool.common.utils.Worker;
import retrofit2.Call;
import retrofit2.Response;

public class DownloadFileCall implements IDownloadFileCall {

    private final float DEFAULT_INCREASE = 0.01f;
    private final retrofit2.Call<ResponseBody> delegate;

    public static long startMills;

    public DownloadFileCall(retrofit2.Call<ResponseBody> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void enqueue(DownloadFileCallback callback) {
        enqueue(DEFAULT_INCREASE, callback);
    }

    @Override
    public void enqueue(float increaseOfProgress, DownloadFileCallback callback) {
        if (callback == null) {
            return;
        }

        delegate.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                long mills = System.currentTimeMillis() - startMills;
                Log.e("rango", "response_mills= " + mills);

                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body == null) {
                        failure();
                        return;
                    }

                    ResponseBody responseBody = new ProgressResponseBody(body) {

                        private long lastProgress = 0;

                        @Override
                        public void onDownload(long length, long current, boolean isDone) {
                            if (current - lastProgress >= increaseOfProgress * length || isDone) {
                                lastProgress = current;
                                downloading(length, current, isDone);
                            }
                        }
                    };

                    long cMills = System.currentTimeMillis();
                    File file = callback.convert(responseBody);
                    long m = System.currentTimeMillis() - cMills;
                    Log.e("rango", "convert_file mills = " + m);

                    if (file != null && file.exists() && file.length() > 0) {
                        success(file);
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

                Worker.postMain(callback::onFailure);
            }

            private void downloading(long length, long current, boolean isDone) {
                Worker.postMain(() -> callback.onDownload(length, current, isDone));
            }

            private void success(File file) {
                Worker.postMain(() -> callback.onSuccess(file));
            }
        });
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }
}

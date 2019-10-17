package rango.tool.androidtool.http.download;

import android.util.Log;

import java.io.File;

import okhttp3.ResponseBody;
import rango.tool.androidtool.http.IFileCall;
import rango.tool.common.utils.Worker;
import retrofit2.Call;
import retrofit2.Response;

public class DownloadFileCall implements IFileCall {

    private final retrofit2.Call<ResponseBody> delegate;

    public static long startMills;

    public DownloadFileCall(retrofit2.Call<ResponseBody> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void enqueue(Object callback) {
        enqueue(DEFAULT_INCREASE, callback);
    }

    @Override
    public void enqueue(float increaseOfProgress, Object callback) {
        if (!(callback instanceof DownloadFileCallback)) {
            return;
        }

        DownloadFileCallback downloadCallback = (DownloadFileCallback) callback;

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
                    File file = downloadCallback.convert(responseBody);
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

                Worker.postMain(downloadCallback::onFailure);
            }

            private void downloading(long length, long current, boolean isDone) {
                Worker.postMain(() -> downloadCallback.onDownload(length, current, isDone));
            }

            private void success(File file) {
                Worker.postMain(() -> downloadCallback.onSuccess(file));
            }
        });
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }
}

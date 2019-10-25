package rango.tool.androidtool.http.lib.download;

import java.io.File;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DownloadFileCall implements IDownloadFileCall {

    private final Call<ResponseBody> delegate;
    private final Executor callBackExecutor;

    public DownloadFileCall(Executor callBackExecutor, Call<ResponseBody> delegate) {
        this.callBackExecutor = callBackExecutor;
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

                    File file = downloadCallback.convert(responseBody);
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
                callBackExecutor.execute(downloadCallback::onFailure);
            }

            private void downloading(long length, long current, boolean isDone) {
                callBackExecutor.execute(() -> downloadCallback.onDownload(length, current, isDone));
            }

            private void success(File file) {
                callBackExecutor.execute(() -> downloadCallback.onSuccess(file));
            }
        });
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }
}

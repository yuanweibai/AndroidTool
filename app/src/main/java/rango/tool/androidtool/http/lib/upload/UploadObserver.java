package rango.tool.androidtool.http.lib.upload;

import java.util.concurrent.atomic.AtomicLong;

import rango.tool.common.utils.Worker;

class UploadObserver {

    @SuppressWarnings("FieldCanBeLocal")
    private static float DEFAULT_INCREASE = 0.01f;

    private UploadFileCallback callback;
    private final long length;
    private AtomicLong current;
    private volatile long last;

    UploadObserver(UploadFileCallback callback, long length) {
        this.callback = callback;
        this.length = length;
        current = new AtomicLong();
        last = 0L;
    }

    synchronized void onUpload(long byteCount) {
        current.addAndGet(byteCount);

        boolean isDone = current.get() == length;
        if (current.get() - last >= DEFAULT_INCREASE * length || isDone) {
            last = current.get();
            final long currentProgress = current.get();
            final boolean done = isDone;
            Worker.postMain(() -> callback.onUpload(length, currentProgress, done));
        }
    }

}

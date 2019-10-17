package rango.tool.androidtool.http.upload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public abstract class ProgressRequestBody extends RequestBody {

    private static final long INVALID_VALUE = -1L;

    private RequestBody delegate;
    private long length = INVALID_VALUE;

    public ProgressRequestBody(RequestBody delegate) {
        this.delegate = delegate;
    }

    public ProgressRequestBody(long length, RequestBody delegate) {
        this.length = length;
        this.delegate = delegate;
    }

    public abstract void onUpload(long length, long current, boolean isDone);

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return delegate.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        Sink forwardingSink = new ForwardingSink(sink) {
            private long current = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                current += byteCount;
                if (length == INVALID_VALUE) {
                    length = contentLength();
                }
                onUpload(length, current, current == length);
            }
        };

        BufferedSink bufferedSink = Okio.buffer(forwardingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }
}

package rango.tool.androidtool.http.lib.upload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public abstract class ProgressRequestBody extends RequestBody {

    private RequestBody delegate;

    public ProgressRequestBody(RequestBody delegate) {
        this.delegate = delegate;
    }

    public abstract void onUpload(long byteCount);

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

        boolean isPercent = sink instanceof Buffer;

        Sink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                if (!isPercent) {
                    onUpload(byteCount);
                }
            }
        };

        BufferedSink bufferedSink = Okio.buffer(forwardingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }
}

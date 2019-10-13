package rango.tool.androidtool.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public abstract class ProgressResponseBody extends ResponseBody {

    private final ResponseBody delegate;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody body) {
        delegate = body;
    }

    public abstract void onDownload(long length, long current, boolean isDone);

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        return delegate.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            Source source = new ForwardingSource(delegate.source()) {
                private long length = -1;
                private long current = 0;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long byteRead = super.read(sink, byteCount);

                    boolean isDone = byteRead == -1;
                    current += !isDone ? byteRead : 0;
                    if (length == -1) {
                        length = contentLength();
                    }
                    onDownload(length, current, isDone);
                    return byteRead;
                }
            };
            bufferedSource = Okio.buffer(source);
        }
        return bufferedSource;
    }

}

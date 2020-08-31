package rango.tool.androidtool.http.lib.signature;

import androidx.annotation.NonNull;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class SignatureInterceptor implements Interceptor {

    private static final String TAG = "SignatureInterceptor";
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        String requestContent = "";
        byte[] bytes = null;
        if (requestBody == null) {
            Log.e(TAG, "no request body!!!");
        } else if (bodyHasUnknownEncoding(request.headers())) {
            Log.e(TAG, "--> END " + request.method() + " (encoded body omitted)");
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = null;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF_8);
            }

            if (charset == null) {
                charset = UTF_8;
            }
            if (isPlaintext(buffer)) {
                bytes = buffer.readByteArray();
                Log.e(TAG, requestContent);
                Log.e(TAG, "--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            } else {
                Log.e(TAG, "--> END " + request.method() + " (binary "
                        + requestBody.contentLength() + "-byte body omid)");
            }
        }
        try {
            Request.Builder builder = request.newBuilder()
                    .addHeader("X-ColorPhone-Signature", SignatureUtils.generateSignature(String.valueOf(System.currentTimeMillis()), bytes));
            request = builder.build();
            return chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}

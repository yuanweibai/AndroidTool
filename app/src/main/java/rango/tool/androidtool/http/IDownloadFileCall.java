package rango.tool.androidtool.http;

import android.support.annotation.FloatRange;

public interface IDownloadFileCall {

    void enqueue(DownloadFileCallback callback);

    void enqueue(@FloatRange(from = 0, to = 1, fromInclusive = false, toInclusive = false) float increaseOfProgress, DownloadFileCallback callback);
}

package rango.tool.androidtool.http.lib.download;

import android.support.annotation.FloatRange;

import rango.tool.androidtool.http.lib.call.CancelableCall;

public interface IDownloadFileCall extends CancelableCall {

    float DEFAULT_INCREASE = 0.01f;

    void enqueue(Object callback);

    void enqueue(@FloatRange(from = 0, to = 1, fromInclusive = false, toInclusive = false) float increaseOfProgress, Object callback);
}

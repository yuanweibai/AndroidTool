package rango.tool.androidtool.http;

import android.support.annotation.FloatRange;

public interface IFileCall extends CancelableCall {

    final float DEFAULT_INCREASE = 0.01f;

    void enqueue(Object callback);

    void enqueue(@FloatRange(from = 0, to = 1, fromInclusive = false, toInclusive = false) float increaseOfProgress, Object callback);
}

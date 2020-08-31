package rango.tool.androidtool.http.lib.call;

import androidx.annotation.NonNull;

public interface Callable<T> extends CancelableCall {

    /**
     * Synchronously send the request and return its response body.
     */
    @NonNull
    T execute() throws Throwable;

    /**
     * Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     */
    void enqueue(Callback<T> callback);
}

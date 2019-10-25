package rango.tool.androidtool.http.lib.call;

public interface RealCallback<T> {

    void onSuccess(T t);

    void onFailure(String errorMsg);
}

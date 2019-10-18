package rango.tool.androidtool.http;

public interface Callback<T> {

    void onFailure(String errorMsg);

    void onSuccess(T t);
}

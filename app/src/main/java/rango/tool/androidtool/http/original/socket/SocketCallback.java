package rango.tool.androidtool.http.original.socket;

public interface SocketCallback {

    void onFailure(String errorMsg);

    void onSuccess(String response);
}

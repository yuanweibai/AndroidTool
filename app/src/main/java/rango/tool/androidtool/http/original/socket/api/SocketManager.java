package rango.tool.androidtool.http.original.socket.api;

import rango.tool.androidtool.http.lib.signature.SignatureUtils;
import rango.tool.androidtool.http.original.socket.SocketCallback;
import rango.tool.androidtool.http.original.socket.SocketHttp;

public class SocketManager {
    private SocketManager() {
    }

    private static class ClassHolder {
        private final static SocketManager S_INTANCE = new SocketManager();
    }

    public static SocketManager getInstance() {
        return SocketManager.ClassHolder.S_INTANCE;
    }

    public void getShows(String url, int pageIndex, SocketCallback callback) {
        SocketHttp socketHttp = new SocketHttp.Builder()
                .url(url)
                .methodGet()
                .addParams("per_page", 5)
                .addParams("page_index", pageIndex)
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Accept-Encoding", "gzip")
                .addHeader("User-Agent", "SocketHttp")
                .addHeader("X-ColorPhone-Signature", SignatureUtils.generateSignature(String.valueOf(System.currentTimeMillis()), null))
                .build();
        socketHttp.enqueue(callback);
    }
}

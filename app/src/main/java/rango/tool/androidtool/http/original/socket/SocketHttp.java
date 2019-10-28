package rango.tool.androidtool.http.original.socket;

import java.util.HashMap;
import java.util.Map;

public class SocketHttp {

    private SocketClient socketClient;

    private SocketHttp(String addressUrl, String method, Map<String, String> headers, Map<String, String> params) {
        socketClient = new SocketClient(addressUrl, method, headers, params);
    }

    public void enqueue(SocketCallback callback) {
        if (socketClient == null) {
            throw new NullPointerException("SocketClient is null!!!");
        }
        socketClient.enqueue(callback);
    }

    public static final class Builder {

        private String url;
        private String method;
        private Map<String, String> params;
        private Map<String, String> headers;

        public Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder methodGet() {
            this.method = "GET";
            return this;
        }

        public Builder methodPost() {
            this.method = "POST";
            return this;
        }

        public Builder addParams(String key, Object value) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(key, String.valueOf(value));
            return this;
        }

        public Builder addHeader(String key, String value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(key, value);
            return this;
        }

        public SocketHttp build() {
            return new SocketHttp(url, method, headers, params);
        }
    }
}

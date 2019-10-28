package rango.tool.androidtool.http.original.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SocketClientCopy {

    private String host;
    private int port;
    private String contextPath;
    private StringBuffer requestMessage;
    private Map<String, String> headers = new LinkedHashMap<>();
    private Map<String, String> params = new HashMap<>();

    public SocketClientCopy(String address) throws MalformedURLException {
        URL url = new URL(address);
        this.port = url.getDefaultPort();
        this.host = url.getHost();
        this.contextPath = url.getPath();
        headers.put("Host", this.host);
    }

    public InputStream execute() throws IOException {
        StringBuffer requestMessage = new StringBuffer();
        List<String> pairs = new ArrayList<>();
        // 处理查询参数
        for (String s : params.keySet()) {
            String pair = s +
                    "=" +
                    params.get(s);
            pairs.add(pair);
        }

        if (pairs.size() > 0) {
            this.contextPath += "?";
            for (int i = 0; i < pairs.size(); i++) {
                if (i == 0) {
                    this.contextPath += pairs.get(i);
                } else {
                    this.contextPath = this.contextPath + "&" + pairs.get(i);
                }
            }
        }

        // http 协议内容
        requestMessage.append("GET " + this.contextPath + " HTTP/1.1" + System.getProperty("line.separator"));
        for (String key : headers.keySet()) {
            requestMessage.append(key);
            requestMessage.append(": ");
            requestMessage.append(headers.get(key));
            requestMessage.append(System.getProperty("line.separator"));
        }

        requestMessage.append("\r\n");

        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(this.host, this.port);
        socket.connect(socketAddress);

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println(requestMessage.toString());
        bufferedWriter.write(requestMessage.toString());
        bufferedWriter.flush();

        return socket.getInputStream();
    }

    public SocketClientCopy addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public SocketClientCopy addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
}

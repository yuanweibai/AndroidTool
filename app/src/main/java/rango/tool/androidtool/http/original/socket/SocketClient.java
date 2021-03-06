package rango.tool.androidtool.http.original.socket;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executor;

class SocketClient {

    private static final String TAG = "SocketClient";

    private String addressUrl;
    private Map<String, String> headers;
    private Map<String, String> params;
    private String method;

    SocketClient(String addressUrl, String method, Map<String, String> headers, Map<String, String> params) {
        this.addressUrl = addressUrl;
        this.method = method;
        this.headers = headers;
        this.params = params;
    }

    void enqueue(SocketCallback callback) {
        if (callback == null) {
            throw new NullPointerException("SocketCallback is NULL!!!");
        }
        if (TextUtils.isEmpty(addressUrl)) {
            callback.onFailure("url is Null");
            return;
        }

        final Executor callbackExecutor = SocketExecutorManager.getInstance().getCallbackExecutor();

        SocketExecutorManager.getInstance().getWorkerExecutorService().execute(new Runnable() {
            @Override public void run() {
                URL url;
                try {
                    url = new URL(addressUrl);
                } catch (MalformedURLException e) {
                    callFailure(e.getMessage());
                    return;
                }

                String host = url.getHost();
                headers.put("Host", host);
                int port = url.getDefaultPort();
                StringBuilder requestBuilder = new StringBuilder();
                StringBuilder pathBuilder = new StringBuilder(url.getPath());

                if (params != null && !params.isEmpty()) {
                    pathBuilder.append("?");
                    int index = 0;
                    int count = params.size();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        pathBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                        if (index < count - 1) {
                            pathBuilder.append("&");
                        }
                        index++;
                    }
                }

                requestBuilder.append(method).append(" ").append(pathBuilder.toString()).append(" HTTP/1.1").append(System.lineSeparator());
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    requestBuilder.append(entry.getKey());
                    requestBuilder.append(": ");
                    requestBuilder.append(entry.getValue());
                    requestBuilder.append(System.lineSeparator());
                }
                requestBuilder.append(System.lineSeparator());

                Socket socket = new Socket();
                SocketAddress address = new InetSocketAddress(host, port);
                BufferedWriter writer = null;
                BufferedReader reader = null;
                try {
                    socket.connect(address);

                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    Log.e(TAG, "\r\n\r\n---------------Request Message------------");
                    String requestMessage = requestBuilder.toString();
                    Log.e(TAG, requestMessage);
                    Log.e(TAG, "---------------Request Message------------end\n" +
                            "\n");
                    writer.write(requestMessage);
                    writer.flush();

                    String response = "null";
                    long mills = System.currentTimeMillis();
                    InputStream inputStream = socket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (TextUtils.isEmpty(line)) {
                            break;
                        }
                        responseBuilder.append(line);
                        responseBuilder.append(System.lineSeparator());
                    }

                    byte[] result = new byte[1024];
                    inputStream.read(result);
                    String resultStr = new String(result, "utf-8");

                    Log.e(TAG, "\r\n\r\n---------------Response Message------------");
                    Log.e(TAG, responseBuilder.toString() + "\n\n result = " + resultStr);
                    Log.e(TAG, "---------------Response Message------------end\n\n");
                    Log.e(TAG, "socket: ----------------request time = " + (System.currentTimeMillis() - mills) + "\n\n\n");
                    callSuccess(response);

                } catch (IOException e) {
                    callFailure(e.getMessage());
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (writer != null) {
                            writer.close();
                        }

                        socket.shutdownOutput();
                        socket.shutdownInput();
                        socket.close();
                    } catch (IOException ignore) {
                    }

                }
            }

            private void callFailure(String errorMsg) {
                callbackExecutor.execute(() -> callback.onFailure(errorMsg));
            }

            private void callSuccess(String response) {
                callbackExecutor.execute(() -> callback.onSuccess(response));
            }
        });


    }
}

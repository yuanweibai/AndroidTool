package rango.tool.androidtool.http.original.socket;

public class HttpMethod {

    public enum Method {
        GET,
        POSt
    }

    public static String toStr(Method method) {
        switch (method) {
            default:
            case GET:
                return "GET";
            case POSt:
                return "POST";
        }
    }
}

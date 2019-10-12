package rango.tool.androidtool.http.bean;

public class TranslationGetBean {

    private int status;

    private content content;

    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }
}

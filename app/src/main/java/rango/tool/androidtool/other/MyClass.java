package rango.tool.androidtool.other;

import android.util.Log;

public class MyClass {

    private String name;

    public MyClass(String name) {
        this.name = name;
        LogEvent.setSource(name);
    }

    public String getname() {
        return name;
    }

    public void log() {
        LogEvent.log();
    }

    private static class LogEvent {
        private static String sSource;

        private static void setSource(String source) {
            sSource = source;
        }

        private static void log() {
            Log.e("rango", "source = " + sSource);
        }
    }
}

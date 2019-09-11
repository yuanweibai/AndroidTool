package rango.tool.common.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Strings {

    public static @NonNull List<String> csvToStringList(@NonNull String listCsv) {
        List<String> strings = new ArrayList<>();
        for (String string : listCsv.split(",")) {
            if (!string.isEmpty()) {
                strings.add(string);
            }
        }
        return strings;
    }

    public static @NonNull String stringListToCsv(@NonNull List<String> strings) {
        if (strings.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(strings.get(0));
        for (int i = 1, upperBound = strings.size(); i < upperBound; i++) {
            builder.append(",").append(strings.get(i));
        }
        return builder.toString();
    }

    public static String[] concat(String[] a, String[] b) {
        String[] c= new String[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}

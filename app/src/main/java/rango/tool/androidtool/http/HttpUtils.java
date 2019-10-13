package rango.tool.androidtool.http;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;

public class HttpUtils {

    public static @Nullable File writeBodyToFile(ResponseBody value, String path) {
        File file = new File(path);
        if (file.exists() && !file.delete()) {
            return null;
        }

        File tmp = new File(file.getPath() + ".tmp");

        if (tmp.exists() && !tmp.delete()) {
            return null;
        }

        if (value == null) {
            return null;
        }

        InputStream is = value.byteStream();
        FileOutputStream fos = null;
        try {
            if (!tmp.createNewFile()) {
                return null;
            }

            fos = new FileOutputStream(tmp);
            byte[] buffer = new byte[8096];
            int c;
            while ((c = is.read(buffer)) != -1) {
                fos.write(buffer, 0, c);
            }
            return tmp.renameTo(file) ? file : null;
        } catch (IOException e) {
            return null;
        } finally {
            Util.closeQuietly(fos);
        }
    }
}

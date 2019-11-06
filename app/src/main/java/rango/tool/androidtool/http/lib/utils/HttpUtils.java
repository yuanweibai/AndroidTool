package rango.tool.androidtool.http.lib.utils;

import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import rango.tool.androidtool.encrypt.EnCryuptUtils;

public class HttpUtils {

    public static boolean isFileValid(File file) {
        return file.exists() && file.length() > 0;
    }

    public static RequestBody getRequestBodyFromJson(String jsonStr) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
    }

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

    private static byte[] header = {'A', 'C', 'B', 'J', 'A', 1, 0};

    public static @Nullable File writeBodyToPlistFile(ResponseBody value, String path) {
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

        InputStream inputStream = value.byteStream();

        byte[] fileHeader = new byte[header.length];
        try {
            inputStream.read(fileHeader);
        } catch (IOException e) {
            return null;
        }

        if (checkFileHeader(fileHeader, header)) {
            inputStream = genUnzipStream(EnCryuptUtils.genDecodeStream(inputStream));
        } else {
            ByteArrayInputStream headerInputStream = new ByteArrayInputStream(fileHeader);
            inputStream = new SequenceInputStream(headerInputStream, inputStream);
        }

        FileOutputStream fos = null;
        try {
            if (!tmp.createNewFile()) {
                return null;
            }

            fos = new FileOutputStream(tmp);
            byte[] buffer = new byte[8096];
            int c;
            while ((c = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, c);
            }
            return tmp.renameTo(file) ? file : null;
        } catch (IOException e) {
            return null;
        } finally {
            Util.closeQuietly(fos);
        }
    }

    private static boolean checkFileHeader(byte[] fileHeader, byte[] header) {
        if (fileHeader.length != header.length) {
            return false;
        }

        for (int i = 0; i < header.length; i++) {
            if (fileHeader[i] != header[i]) {
                return false;
            }
        }
        return true;
    }

    private static InputStream genUnzipStream(InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }

        return new InflaterInputStream(inputStream, new Inflater());
    }
}

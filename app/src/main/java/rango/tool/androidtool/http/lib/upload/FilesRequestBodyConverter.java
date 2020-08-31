package rango.tool.androidtool.http.lib.upload;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rango.tool.androidtool.http.lib.utils.HttpUtils;
import retrofit2.Converter;

public class FilesRequestBodyConverter implements Converter<HashMap<String, Object>, RequestBody> {

    public static final String KEY_FILE_PATH_MAP = "key_file_path_map";
    public static final String KEY_UPLOAD_FILE_CALLBACK = "key_upload_file_callback";

    @Override
    public RequestBody convert(@NonNull HashMap<String, Object> value) throws IOException {
        if (value.isEmpty() || !value.containsKey(KEY_FILE_PATH_MAP) || !value.containsKey(KEY_UPLOAD_FILE_CALLBACK)) {
            return null;
        } else {
            return getMultipartBody(value);
        }
    }

    private synchronized MultipartBody getMultipartBody(HashMap<String, Object> value) {

        UploadFileCallback uploadFileCallback = (UploadFileCallback) value.get(KEY_UPLOAD_FILE_CALLBACK);

        @SuppressWarnings("unchecked")
        HashMap<String, String> filePathMap = (HashMap<String, String>) value.get(KEY_FILE_PATH_MAP);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        long totalLength = 0;

        assert filePathMap != null;
        HashMap<String, File> fileMap = new HashMap<>(filePathMap.size());

        for (HashMap.Entry<String, String> entry : filePathMap.entrySet()) {
            String key = entry.getKey();
            String filePath = entry.getValue();
            File file = new File(filePath);
            if (!HttpUtils.isFileValid(file)) {
                continue;
            }
            totalLength += file.length();
            fileMap.put(key, file);
        }

        UploadObserver observer = new UploadObserver(uploadFileCallback, totalLength);

        for (HashMap.Entry<String, File> entry : fileMap.entrySet()) {
            String key = entry.getKey();
            File file = entry.getValue();
            ProgressRequestBody body = new ProgressRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), file)) {

                @Override
                public void onUpload(long byteCount) {
                    observer.onUpload(byteCount);
                }
            };
            builder.addFormDataPart(key, file.getName(), body);
        }

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String key = entry.getKey();
            if (KEY_FILE_PATH_MAP.equals(key) || KEY_UPLOAD_FILE_CALLBACK.equals(key)) {
                continue;
            }

            builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
        }

        return builder.build();
    }
}

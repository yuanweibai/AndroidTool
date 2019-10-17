package rango.tool.androidtool.http.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class FilesRequestBodyConverter implements Converter<HashMap<String, Object>, RequestBody> {

    public static final String KEY_FILE_PATH_LIST = "key_file_path_list";
    public static final String KEY_UPLOAD_FILE_CALLBACK = "key_upload_file_callback";

    private UploadFileCallback uploadFileCallback;

    @Override
    public RequestBody convert(HashMap<String, Object> value) throws IOException {

        Object callback = value.get(KEY_UPLOAD_FILE_CALLBACK);
        if (!(callback instanceof UploadFileCallback)) {
            throw new IllegalStateException("There is no UploadFileCallback!!!");
        }

        uploadFileCallback = (UploadFileCallback) callback;

        Object filePaths = value.get(KEY_FILE_PATH_LIST);
        if (!(filePaths instanceof ArrayList)) {
            throw new IllegalStateException("There is no FilePathList!!!");
        }

        @SuppressWarnings("unchecked")
        List<String> filePathList = (List<String>) filePaths;

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        long totalLength = 0;
        List<File> fileList = new ArrayList<>(value.size());
        for (String filePath : filePathList) {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                continue;
            }
            totalLength += file.length();
            fileList.add(file);
        }

        for (File file : fileList) {
            ProgressRequestBody body = new ProgressRequestBody(totalLength, RequestBody.create(MultipartBody.FORM, file)) {
                @Override
                public void onUpload(long length, long current, boolean isDone) {
                    uploadFileCallback.onUpload(length, current, isDone);
                }
            };

            builder.addFormDataPart("file", file.getName(), body);
        }

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String key = entry.getKey();
            if (KEY_FILE_PATH_LIST.equals(key) || KEY_UPLOAD_FILE_CALLBACK.equals(key)) {
                continue;
            }

            builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
        }

        return builder.build();
    }
}

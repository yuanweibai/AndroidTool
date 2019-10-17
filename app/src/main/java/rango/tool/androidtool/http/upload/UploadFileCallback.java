package rango.tool.androidtool.http.upload;

public interface UploadFileCallback {

    void onSuccess();

    void onUpload(long length, long current, boolean isDone);

    void onFailure(String errorMsg);
}

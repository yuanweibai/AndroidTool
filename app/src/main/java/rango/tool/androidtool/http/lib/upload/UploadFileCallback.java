package rango.tool.androidtool.http.lib.upload;

public interface UploadFileCallback {

    void onSuccess();

    void onUpload(long length, long current, boolean isDone);

    void onFailure(String errorMsg);
}

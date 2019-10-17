package rango.tool.androidtool.http.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.http.CancelableCall;
import rango.tool.androidtool.http.download.DownloadFileCall;
import rango.tool.androidtool.http.download.DownloadFileCallback;
import rango.tool.androidtool.http.HttpManager;
import rango.tool.androidtool.http.HttpUtils;
import rango.tool.androidtool.http.upload.UploadFileCallback;
import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpActivity extends BaseActivity {

    private Button downloadBtn;
    private TextView progressValueText;
    private CancelableCall downloadApkCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_layout);

        findViewById(R.id.translate_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                HttpManager.getInstance().translate("Thanks!!!", new Callback<TranslationBean>() {
                    @Override
                    public void onResponse(Call<TranslationBean> call, Response<TranslationBean> response) {
                        TranslationBean bean = response.body();
                        Toast.makeText(HttpActivity.this, "success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<TranslationBean> call, Throwable t) {
                        Toast.makeText(HttpActivity.this, "failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        findViewById(R.id.translate_get_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpManager.getInstance().translateGet(new Callback<TranslationGetBean>() {
                    @Override
                    public void onResponse(Call<TranslationGetBean> call, Response<TranslationGetBean> response) {
                        TranslationGetBean bean = response.body();
                        Toast.makeText(HttpActivity.this, "success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<TranslationGetBean> call, Throwable t) {
                        Toast.makeText(HttpActivity.this, "failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                long id = Thread.currentThread().getId();
                Log.e("Main", "main thread id = " + id);
                Toast.makeText(HttpActivity.this, "Click: threadId = " + id, Toast.LENGTH_LONG).show();

                HttpManager.getInstance().login("singleman", "123456", new Callback<LoginInfoBean>() {
                    @Override
                    public void onResponse(Call<LoginInfoBean> call, Response<LoginInfoBean> response) {
                        long id = Thread.currentThread().getId();
                        LoginInfoBean bean = response.body();
                        Toast.makeText(HttpActivity.this, "success: threadId = " + id, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<LoginInfoBean> call, Throwable t) {
                        long id = Thread.currentThread().getId();
                        Toast.makeText(HttpActivity.this, "failure: threadId = " + id, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        progressValueText = findViewById(R.id.progress_value_text);
        downloadBtn = findViewById(R.id.download_file_btn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                downloadBtn.setText("取消下载");
                String url = "http://shouji.360tpcdn.com/181115/4dc46bd86bef036da927bc59680f514f/com.ss.android.ugc.aweme_330.apk";

                if (cancelDownloadCall()) {
                    return;
                }

                DownloadFileCall.startMills = System.currentTimeMillis();
                progressValueText.setText("%0");
                downloadApkCall = HttpManager.getInstance().downloadFile(url, new DownloadFileCallback() {
                    @Override
                    public void onSuccess(File file) {
                        Toast.makeText(ToolApplication.getContext(), "Success", Toast.LENGTH_LONG).show();
                        downloadBtn.setText("下载完成");
                    }

                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public @NonNull File convert(ResponseBody responseBody) {
                        final String filePath = new File(ToolApplication.getContext().getExternalCacheDir(), "test_douyin.apk").getPath();
                        return HttpUtils.writeBodyToFile(responseBody, filePath);
                    }

                    @Override
                    public void onDownload(long length, long current, boolean isDone) {
                        float process = current / (float) length;
                        String processStr = "%" + (int) (process * 100);
                        Log.e("rango", "length = " + length + ", currnet = " + current + ", process = " + processStr + ",isDone = " + isDone);
                        progressValueText.setText(processStr);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(ToolApplication.getContext(), "Failure", Toast.LENGTH_LONG).show();
                        downloadBtn.setText("下载文件");
                        progressValueText.setText("%0");
                    }
                });
            }
        });

        findViewById(R.id.upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSingleFile();
            }
        });

        findViewById(R.id.upload_description_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileAndDescription();
            }
        });

        findViewById(R.id.upload_more_file_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMoreFile();
            }
        });

        findViewById(R.id.upload_file_width_progress_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFileWidthProgress();
            }
        });

        findViewById(R.id.upload_more_files_with_progress_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                uploadMoreFilesWithProgress();
            }
        });
    }

    private void uploadMoreFilesWithProgress(){
        List<String> filePathList= new ArrayList<>();

        HttpManager.getInstance().uploadMoreFilesWithProgress(filePathList, "ihandy", "man", new UploadFileCallback() {
            @Override
            public void onSuccess() {
                Log.e("rango", "uploadMoreFilesWithProgress: successful!!!");
            }

            @Override
            public void onUpload(long length, long current, boolean isDone) {
                Log.e("rango", "uploadMoreFilesWithProgress: uploading..." + (current / (float) length));

            }

            @Override
            public void onFailure(String errorMsg) {
                Log.e("rango", "uploadMoreFilesWithProgress: failure, " + errorMsg);
            }
        });
    }

    private void uploadSingleFile() {
        String filePath = "";
        HttpManager.getInstance().uploadSingleFile(filePath, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HttpActivity.this, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HttpActivity.this, "Failure: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HttpActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadFileAndDescription() {
        String filePath = "";
        HttpManager.getInstance().uploadFileAndDescription("ihandy", "man", "1993-2-9", filePath, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HttpActivity.this, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HttpActivity.this, "Failure: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HttpActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void uploadMoreFile() {
        List<String> filePathList = new ArrayList<>();
        filePathList.add("");
        filePathList.add("");
        filePathList.add("");
        filePathList.add("");

        HttpManager.getInstance().uploadMoreFile(filePathList, "ihandy", "man", new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HttpActivity.this, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HttpActivity.this, "Failure: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HttpActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        /**
         * uploadMoreFileByPart() 方法也是可以上传多个文件的
         */
//        HttpManager.getInstance().uploadMoreFileByPart(filePathList,"ihandy", "man", new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(HttpActivity.this, "Success", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(HttpActivity.this, "Failure: " + response.message(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(HttpActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void uploadFileWidthProgress() {
        String filePath = "";

        HttpManager.getInstance().uploadFileWithProgress(filePath, "iHandy", "man", new UploadFileCallback() {
            @Override
            public void onSuccess() {
                Log.e("rango", "uploadFileWidthProgress: successful!!!");
            }

            @Override
            public void onUpload(long length, long current, boolean isDone) {
                Log.e("rango", "uploadFileWidthProgress: uploading..." + (current / (float) length));
            }

            @Override
            public void onFailure(String errorMsg) {
                Log.e("rango", "uploadFileWidthProgress: failure, " + errorMsg);
            }
        });
    }

    private boolean cancelDownloadCall() {
        if (downloadApkCall != null) {
            downloadApkCall.cancel();
            downloadApkCall = null;
            return true;
        }
        return false;
    }
}

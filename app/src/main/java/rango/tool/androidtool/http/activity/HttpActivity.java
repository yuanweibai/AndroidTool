package rango.tool.androidtool.http.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import okhttp3.ResponseBody;
import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.http.DownloadFileCallback;
import rango.tool.androidtool.http.HttpManager;
import rango.tool.androidtool.http.HttpUtils;
import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpActivity extends BaseActivity {

    private Button downloadBtn;

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

        downloadBtn = findViewById(R.id.download_file_btn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                downloadBtn.setText("%0");
                String url = "http://shouji.360tpcdn.com/181115/4dc46bd86bef036da927bc59680f514f/com.ss.android.ugc.aweme_330.apk";

                HttpManager.getInstance().downloadFile(url, new DownloadFileCallback() {
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
                        downloadBtn.setText(processStr);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(ToolApplication.getContext(), "Failure", Toast.LENGTH_LONG).show();
                        downloadBtn.setText("下载文件");
                    }
                });
            }
        });
    }
}

package rango.tool.androidtool.encrypt;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.File;

import okhttp3.ResponseBody;
import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.http.api.HttpManager;
import rango.tool.androidtool.http.lib.download.DownloadFileCallback;
import rango.tool.androidtool.http.lib.utils.HttpUtils;

public class EncryptActivity extends BaseActivity {

    private static final String TAG = "EncryptActivity";
    private static final String URL = "http://cdn.ihandysoft.cn/light2019/apps/apkcolorphone/configs/yyb_config_colorphone_1.0.0.sp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_layout);

        findViewById(R.id.download_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                downloadFile();
            }
        });
    }

    private void downloadFile() {

        HttpManager.getInstance().downloadFile(URL, new DownloadFileCallback() {
            @Override
            public void onSuccess(File file) {

                Log.e(TAG, "Successfully!!!");
            }

            @Nullable
            @Override
            public File convert(ResponseBody responseBody) {
                final String filePath = new File(ToolApplication.getContext().getExternalCacheDir(), "aa-config.plist").getPath();
                return HttpUtils.writeBodyToPlistFile(responseBody, filePath);
            }

            @Override public void onDownload(long length, long current, boolean isDone) {

            }

            @Override public void onFailure() {
                Log.e(TAG, "Failure!!!");
            }
        });

    }
}

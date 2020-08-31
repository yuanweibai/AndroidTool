package rango.tool.androidtool.http.activity;

import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import rango.tool.androidtool.http.api.HttpManager;
import rango.tool.androidtool.http.api.IHttpConstant;
import rango.tool.androidtool.http.api.bean.AllThemeBean;
import rango.tool.androidtool.http.api.bean.AllUserThemeBean;
import rango.tool.androidtool.http.api.bean.LoginUserBean;
import rango.tool.androidtool.http.lib.call.Callable;
import rango.tool.androidtool.http.lib.call.Callback;
import rango.tool.androidtool.http.lib.call.CancelableCall;
import rango.tool.androidtool.http.lib.download.DownloadFileCallback;
import rango.tool.androidtool.http.lib.upload.UploadFileCallback;
import rango.tool.androidtool.http.lib.utils.HttpUtils;
import rango.tool.androidtool.http.original.socket.SocketCallback;
import rango.tool.androidtool.http.original.socket.api.SocketManager;

public class HttpActivity extends BaseActivity {

    private static final String TAG = "HttpActivity";

    private Button downloadBtn;
    private TextView progressValueText;
    private CancelableCall downloadApkCall;

    private Callable<ResponseBody> uploadCall = null;

    private static final boolean IS_SOCKET = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_layout);
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.edit_user_info_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserInfo();
            }
        });
        findViewById(R.id.get_user_info_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo();
            }
        });
        findViewById(R.id.get_show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IS_SOCKET) {
                    getShowBySocket();
                } else {
                    getShow();
                }
            }
        });
        findViewById(R.id.upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        findViewById(R.id.cancel_upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                cancelUpload();
            }
        });
        findViewById(R.id.get_upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpload();
            }
        });
        findViewById(R.id.get_publish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPublish();
            }
        });

        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        progressValueText = findViewById(R.id.progress_value_text);
        downloadBtn = findViewById(R.id.download_file_btn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                downloadFile();
            }
        });
    }

    private void login() {

        HttpManager.getInstance().login("code", new Callback<LoginUserBean>() {
            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override
            public void onSuccess(LoginUserBean loginInfoBean) {
                // Must to save token and uid
                if (loginInfoBean != null && loginInfoBean.getUser_info() != null) {
                    HttpManager.getInstance().saveUserTokenAndUid(loginInfoBean.getToken(), loginInfoBean.getUser_info().getUser_id());
                }
                success();

            }
        });
    }

    private void editUserInfo() {

        LoginUserBean.UserInfoBean userInfoBean = new LoginUserBean.UserInfoBean();
        userInfoBean.setName("hhhhh");
        userInfoBean.setBirthday("1993-10-20");
        userInfoBean.setGender(IHttpConstant.GENDER_MAN);
        userInfoBean.setSignature("fadj fslkdfsaf fasdfa");

        String headImgFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ugc/wallpaper.jpg";

        HttpManager.getInstance().editUserInfo(userInfoBean, headImgFilePath, new Callback<ResponseBody>() {
            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                success();
            }
        });
    }

    private void getUserInfo() {
        HttpManager.getInstance().getSelfUserInfo(new Callback<LoginUserBean>() {
            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override
            public void onSuccess(LoginUserBean bean) {
                success();
            }
        });
    }

    private void getShowBySocket() {

        String url = "http://dev-colorphone-service.appcloudbox.net/shows";
        SocketManager.getInstance().getShows(url, 1, new SocketCallback() {
            @Override public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override public void onSuccess(String response) {
                success();
            }
        });
    }

    private void getShow() {
        HttpManager.getInstance().getAllThemes(1, new Callback<AllThemeBean>() {
            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override
            public void onSuccess(AllThemeBean allThemeBean) {
                success();
            }
        });
    }

    private void upload() {

        String videoFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ugc/dog.mp4";
        String audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ugc/fall_master_death.mp3";
        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ugc/wallpaper.jpg";
        String name = "FirstVideo";

        uploadCall = HttpManager.getInstance().uploadVideos(videoFilePath, audioFilePath, imageFilePath, name, new UploadFileCallback() {
            @Override
            public void onSuccess() {
                success();
            }

            @Override
            public void onUpload(long length, long current, boolean isDone) {
                String progress = (int) (current / (float) length * 100) + "%";
                Log.e(TAG, "oUpload: length = " + length + ", current = " + current + ", progress = " + progress + ", isDone = " + isDone);
            }

            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }
        });
    }

    private void cancelUpload() {
        if (uploadCall != null) {
            uploadCall.cancel();
            uploadCall = null;
        }
    }

    private void getUpload() {
        HttpManager.getInstance().getUserUploadedVideos(1, new Callback<AllUserThemeBean>() {
            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override public void onSuccess(AllUserThemeBean allUserThemeBean) {
                success();
            }
        });
    }

    private void getPublish() {
        HttpManager.getInstance().getUserPublishedVideos(1, new Callback<AllUserThemeBean>() {
            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override
            public void onSuccess(AllUserThemeBean allUserThemeBean) {
                success();
            }
        });
    }

    private void delete() {
        List<Long> themeIdList = new ArrayList<>();
        themeIdList.add(10003L);
        themeIdList.add(10005L);
        themeIdList.add(10007L);
        HttpManager.getInstance().deleteUserVideos(themeIdList, new Callback<ResponseBody>() {
            @Override
            public void onFailure(String errorMsg) {
                failure(errorMsg);
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                success();
            }
        });

    }

    private void downloadFile() {
        downloadBtn.setText("取消下载");
        String url = "http://shouji.360tpcdn.com/181115/4dc46bd86bef036da927bc59680f514f/com.ss.android.ugc.aweme_330.apk";

        if (cancelDownloadCall()) {
            return;
        }

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

    private void success() {
        Toast.makeText(this, "Successfully!!!", Toast.LENGTH_SHORT).show();
    }

    private void failure(String msg) {
        Toast.makeText(this, "Failure!!!, msg = " + msg, Toast.LENGTH_SHORT).show();
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

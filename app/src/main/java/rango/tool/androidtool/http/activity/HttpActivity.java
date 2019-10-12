package rango.tool.androidtool.http.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.http.HttpManager;
import rango.tool.androidtool.http.bean.LoginInfoBean;
import rango.tool.androidtool.http.bean.TranslationBean;
import rango.tool.androidtool.http.bean.TranslationGetBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpActivity extends BaseActivity {

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
                HttpManager.getInstance().login("singleman", "123456", new Callback<LoginInfoBean>() {
                    @Override
                    public void onResponse(Call<LoginInfoBean> call, Response<LoginInfoBean> response) {
                        LoginInfoBean bean = response.body();
                        Toast.makeText(HttpActivity.this, "success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<LoginInfoBean> call, Throwable t) {
                        Toast.makeText(HttpActivity.this, "failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}

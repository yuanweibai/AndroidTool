package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.provider.OtherProcessProvider;

public class ProviderTestActivity extends BaseActivity {

    private int count = 0;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_test);

        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getContentResolver().call(OtherProcessProvider.createRemoteConfigContentUri(), "rango_r", null, null);
                if (bundle != null) {
                    String value = bundle.getString("rango_read");
                    Toast.makeText(ProviderTestActivity.this, value, Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.write_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("rango_write", "main process-" + (count++));
                getContentResolver().call(OtherProcessProvider.createRemoteConfigContentUri(), "rango_w", null, bundle);
            }
        });
    }
}

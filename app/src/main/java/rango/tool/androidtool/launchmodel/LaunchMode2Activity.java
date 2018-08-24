package rango.tool.androidtool.launchmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class LaunchMode2Activity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode_2_layout);

        findViewById(R.id.btn_1).setOnClickListener(v -> startActivity(LaunchMode1Activity.class));
        findViewById(R.id.btn_3).setOnClickListener(v -> {
            Intent intent = new Intent(LaunchMode2Activity.this, LaunchMode3Activity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}

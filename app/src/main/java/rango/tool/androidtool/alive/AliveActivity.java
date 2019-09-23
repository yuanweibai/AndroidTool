package rango.tool.androidtool.alive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.alive.process.AliveService;
import rango.tool.androidtool.alive.process.LocalService;
import rango.tool.androidtool.base.BaseActivity;

public class AliveActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alive_layout);

        findViewById(R.id.double_process_alive_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startService(new Intent(AliveActivity.this, LocalService.class));
                startService(new Intent(AliveActivity.this, AliveService.class));
            }
        });
    }
}

package rango.tool.androidtool.memoryleak;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class MemoryLeakActivity extends BaseActivity {

    public static Activity activity;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak_layout);

        findViewById(R.id.btn_1).setOnClickListener(v -> startActivity(HandlerMemoryLeakActivity.class));
        findViewById(R.id.btn_2).setOnClickListener(v -> startActivity(StaticMemoryLeakActivity.class));
    }
}

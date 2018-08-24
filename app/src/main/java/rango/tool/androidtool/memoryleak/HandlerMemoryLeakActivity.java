package rango.tool.androidtool.memoryleak;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import rango.tool.androidtool.base.BaseActivity;

public class HandlerMemoryLeakActivity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {

        }, 1000000);
    }
}

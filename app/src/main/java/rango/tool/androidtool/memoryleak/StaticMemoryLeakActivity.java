package rango.tool.androidtool.memoryleak;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.base.BaseActivity;

public class StaticMemoryLeakActivity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MemoryLeakActivity.activity = this;
    }
}

package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class ExceptionActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_layout);

        findViewById(R.id.start_exception_btn).setOnClickListener(v -> {
            checkZipPathAttack();
            Log.e("rango", "jldfajdlkfjalksdfjlakdjflakjdf");
        });
    }

    public static void checkZipPathAttack() {
        int i = 0;
        if (i == 0) {
            throw new SecurityException("Zip Path Traversal attack, visit https://support.google.com/faqs/answer/9294009!!!");
        }
    }
}

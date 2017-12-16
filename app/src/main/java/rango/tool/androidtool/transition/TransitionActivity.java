package rango.tool.androidtool.transition;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class TransitionActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        findViewById(R.id.image_btn).setOnClickListener(v -> startActivity(ImageActivity.class));

        findViewById(R.id.image_list_btn).setOnClickListener(v -> startActivity(ImageListActivity.class));
    }
}

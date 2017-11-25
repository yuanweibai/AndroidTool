package rango.tool.androidtool.imageview;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

/**
 * Created by baiyuanwei on 17/11/18.
 */

public class ImageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);
    }
}

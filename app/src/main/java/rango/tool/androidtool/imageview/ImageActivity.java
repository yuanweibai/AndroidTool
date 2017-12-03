package rango.tool.androidtool.imageview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;


public class ImageActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);
        imageView = findViewById(R.id.webp_img);
        imageView.setImageResource(R.drawable.app_lock_guide_image_1);
    }
}

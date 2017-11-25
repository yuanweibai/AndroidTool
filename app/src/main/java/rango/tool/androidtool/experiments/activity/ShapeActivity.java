package rango.tool.androidtool.experiments.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import rango.tool.androidtool.GlideApp;
import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

/**
 * Created by baiyuanwei on 17/11/20.
 */

public class ShapeActivity extends BaseActivity {
    private static final String picUrl = "http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/chalk.png";
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_layout);
        imageView = findViewById(R.id.image_view);

        imageView.setImageResource(R.drawable.load_image_default);

        GlideApp.with(ShapeActivity.this)
                .load(picUrl)
                .placeholder(R.drawable.load_image_default)
                .error(R.drawable.load_image_error)
                .into(imageView);

    }
}

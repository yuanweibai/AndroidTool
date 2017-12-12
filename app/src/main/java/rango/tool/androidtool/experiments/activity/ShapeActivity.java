package rango.tool.androidtool.experiments.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.ImageViewTarget;

import rango.tool.androidtool.GlideApp;
import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

public class ShapeActivity extends BaseActivity {
    private static final String picUrl = "http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/chalk.png";
    private ImageView imageView;
    private TextView tempText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_layout);
        imageView = findViewById(R.id.image_view);
        tempText = findViewById(R.id.temp_text);

        GlideApp.with(ShapeActivity.this)
                .load(picUrl)
                .placeholder(R.drawable.load_image_default)
                .error(R.drawable.load_image_error)
                .into(new ImageViewTarget<Drawable>(imageView) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        Log.e("rango", "Glide#into()#setResource()");
                        this.view.setImageDrawable(resource);
                        tempText.setVisibility(View.VISIBLE);
                    }


                });

    }
}

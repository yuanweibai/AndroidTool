package rango.tool.androidtool.experiments.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.util.BitmapUtils;

public class ShapeActivity extends BaseActivity {
    private static final String picUrl = "http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/chalk.png";
    private ImageView imageView;
    private TextView tempText;
    private ImageView wallpaperImageView;
    private ImageView toyImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_layout);
        imageView = findViewById(R.id.image_view);
        tempText = findViewById(R.id.temp_text);
        wallpaperImageView = findViewById(R.id.wallpaper_image_view);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lucky_toy_uncollected);
        Bitmap result = BitmapUtils.addWhiteCircleBorderToBitmap(bitmap, 20);
        wallpaperImageView.setImageBitmap(result);
        imageView.setImageBitmap(bitmap);

        toyImageView = findViewById(R.id.wallpaper_image_view_2);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.game_promotion_top_part_man);
        Bitmap result2 = BitmapUtils.addWhiteCircleBorderToBitmap(bitmap2, 20);
        toyImageView.setImageBitmap(result2);

//        GlideApp.with(ShapeActivity.this)
//                .load(picUrl)
//                .placeholder(R.drawable.load_image_default)
//                .error(R.drawable.load_image_error)
//                .into(new ImageViewTarget<Drawable>(imageView) {
//                    @Override
//                    protected void setResource(@Nullable Drawable resource) {
//                        Log.e("rango", "Glide#into()#setResource()");
//                        this.view.setImageDrawable(resource);
//                        tempText.setVisibility(View.VISIBLE);
//                    }
//
//
//                });

    }
}

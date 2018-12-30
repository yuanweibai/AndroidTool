package rango.tool.androidtool.experiments.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;

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
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lucky_toy_uncollected);
//        Bitmap result = BitmapUtils.addWhiteCircleBorderToBitmap(bitmap, 20);
//        wallpaperImageView.setImageBitmap(result);
//        imageView.setImageBitmap(bitmap);

//            InputStream in = null;
//            in = getResources().getAssets().open("anim_drar.webp");
//            final FrameSequenceDrawable drawable = new FrameSequenceDrawable(in);
//            drawable.setLoopCount(1);
//            drawable.setLoopBehavior(FrameSequenceDrawable.LOOP_FINITE);
//            drawable.setOnFinishedListener(new FrameSequenceDrawable.OnFinishedListener() {
//                @Override
//                public void onFinished(FrameSequenceDrawable frameSequenceDrawable) {
//
//                }
//            });

        findViewById(R.id.anim_btn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                wallpaperImageView.setTranslationX(40);
                Log.e("rnago", "translationX = " + wallpaperImageView.getTranslationX());
                ObjectAnimator animator = ObjectAnimator.ofFloat(wallpaperImageView, "translationX", 50);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationEnd(Animator animation) {
                        Log.e("rnago", "end-translationX = " + wallpaperImageView.getTranslationX());
                    }
                });
                animator.setDuration(300);
                animator.start();
            }
        });

        RequestOptions options =
                new RequestOptions()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this)
                .load("file:///android_asset/anim.webp")
                .into(wallpaperImageView);
//        Glide.with(this)
//                .load("file:///android_asset/sticker1.webp")
//                .apply(options).transition(new DrawableTransitionOptions().crossFade(200))
//                .into(wallpaperImageView);

//        GlideApp.with(this)
//                .load(getResources().getDrawable(R.drawable.lucky_toy_uncollected))
//                .into(wallpaperImageView);

//            wallpaperImageView.setImageResource(R.drawable.lucky_toy_uncollected);


        toyImageView = findViewById(R.id.wallpaper_image_view_2);
//        toyImageView
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.game_promotion_top_part_man);
//        Bitmap result2 = BitmapUtils.addWhiteCircleBorderToBitmap(bitmap2, 20);
//        toyImageView.setImageBitmap(result2);

//        String url = "http://omsproductionimg.yangkeduo./images/label/610/GS4X5Ojt5TlVPuAtNGqr2hywByGs2FHN.jpg@120w_1l_50Q.webp";
//        GlideApp.with(ShapeActivity.this)
//                .load(url)
//                .placeholder(R.drawable.lucky_toy_uncollected)
//                .error(R.drawable.lucky_toy_uncollected)
//                .into(toyImageView);

    }
}

package rango.tool.androidtool.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.transition.Transition;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import rango.tool.androidtool.GlideApp;
import rango.tool.androidtool.R;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.data.DataProvider;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ImageDetailActivity extends BaseActivity {

    private ImageView imageView;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text);
        button = findViewById(R.id.button);

        if (savedInstanceState == null) {
            transitionEndListener();
        }

        if (getIntent() != null) {
            String url = getIntent().getStringExtra(ImageItemView.KEY_IMAGE_URL);
            GlideApp.with(this)
                    .load(url)
                    .into(imageView);
        }
        textView.setText(DataProvider.getMsg());
    }

    private void transitionEndListener() {
        button.setScaleX(0);
        button.setScaleY(0);
        getWindow().getEnterTransition().addListener(new CustomTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);
                button.animate().scaleX(1).scaleY(1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        button.animate().scaleY(0).scaleX(0).setListener(new CustomTransitionListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                supportFinishAfterTransition();
            }
        });
    }
}

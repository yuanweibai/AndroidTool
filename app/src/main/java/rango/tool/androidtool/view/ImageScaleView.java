package rango.tool.androidtool.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import rango.tool.common.utils.ScreenUtils;

public class ImageScaleView extends FrameLayout {

    private ImageView imageView;
    private int left;
    private int top;
    private int viewWidth;
    private int viewHeight;
    private int screenWidth;
    private int screenHeight;
    private float scaleX;
    private float scaleY;
    private float translationX;
    private float translationY;

    public ImageScaleView(@NonNull Context context) {
        this(context, null);
    }

    public ImageScaleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageScaleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setVisibility(INVISIBLE);
        addView(imageView, params);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void downClick() {

    }

    private void upClick() {
        initSize();

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", scaleX);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", scaleY);
        PropertyValuesHolder translationXHolder = PropertyValuesHolder.ofFloat("translationX", translationX);
        PropertyValuesHolder translationYHolder = PropertyValuesHolder.ofFloat("translationY", translationY);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, scaleXHolder, scaleYHolder, translationXHolder, translationYHolder);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setVisibility(VISIBLE);
            }
        });
        animator.setDuration(3000);
        animator.start();

    }

    private void initSize() {
        viewWidth = imageView.getWidth();
        viewHeight = imageView.getHeight();
        screenWidth = ScreenUtils.getScreenWidthPx();
        screenHeight = ScreenUtils.getScreenHeightPx();

        scaleX = screenWidth / (float) viewWidth;
        scaleY = screenHeight / (float) viewHeight;

        translationX = screenWidth / 2f - (left + viewWidth / 2f);
        translationY = screenHeight / 2f - (top + viewHeight / 2f);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        //nothing
    }

    public void setImageDrawable(int drawableId) {
        imageView.setImageResource(drawableId);
    }

}

package rango.tool.androidtool.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import rango.tool.common.utils.ScreenUtils;

public class CircleEffectTextView extends AppCompatTextView {

    private View mEffectView;
    private float phoneHeight;
    private float phoneWidth;
    private float maxD;

    public CircleEffectTextView(Context context) {
        this(context, null, 0);
    }

    public CircleEffectTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleEffectTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        phoneHeight = ScreenUtils.getScreenHeightPx();
        phoneWidth = ScreenUtils.getScreenWidthPx();
    }

    public void setEffectView(View view) {
        mEffectView = view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getRawX();
            float y = event.getRawY();

            startAnim(x, y);
            return true;
        }
        return true;
    }

    private void startAnim(float x, float y) {
        int[] location = new int[2];
        getLocationOnScreen(location);

        float r1 = (float) Math.sqrt(x * x + y * y);
        float r2 = (float) Math.sqrt((phoneWidth - x) * (phoneWidth - x) + y * y);
        float r3 = (float) Math.sqrt((phoneWidth - x) * (phoneWidth - x) + (phoneHeight - y) * (phoneHeight - y));
        float r4 = (float) Math.sqrt(x * x + (phoneHeight - y) * (phoneHeight - y));
        float r = r1;
        if (r2 > r) {
            r = r2;
        }

        if (r3 > r) {
            r = r3;
        }

        if (r4 > r) {
            r = r4;
        }
        if (!isOut(location[0], location[1], x, y)) {
            return;
        }

        float size = 600;
        Log.e("rango", "size = " + size + ", scaleY = " + mEffectView.getScaleY() + ", scaleX = " + mEffectView.getScaleX());
        int left = (int) (x - size / 2f);
        int top = (int) (y - ScreenUtils.getStatusBarHeight() - size / 2f);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEffectView.getLayoutParams();
        params.leftMargin = left;
        params.topMargin = top;
        mEffectView.setLayoutParams(params);

        float startScale = ScreenUtils.px2dp(20) / size;
        float maxSize = 2 * r;
        float finalScale = maxSize / size;

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", startScale, finalScale);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", startScale, finalScale);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mEffectView, scaleXHolder, scaleYHolder);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mEffectView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mEffectView.setVisibility(GONE);
                mEffectView.setScaleX(1f);
                mEffectView.setScaleY(1f);
            }
        });
        animator.start();
    }

    private boolean isOut(int left, int top, float x, float y) {
        int w = getWidth();
        int h = getHeight();

        if (x < left || x > left + w
                || y < top || y > top + h) {
            return false;
        }
        return true;
    }


}

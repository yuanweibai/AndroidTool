package rango.tool.androidtool.earning;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

public class EarningBean {

    private int x;
    private int y;

    private int endX;
    private int endY;

    private int startX;
    private int startY;

    private float scale;

    EarningBean(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        x = startX;
        y = startY;
        scale = 0f;
    }

    float getScale() {
        return scale;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public void start(long delay) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.2f, 1f);
        animator.addUpdateListener(animation -> scale = (float) animation.getAnimatedValue());
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startTranslate();
            }
        });
        animator.setDuration(800);
        animator.setStartDelay(delay);
        animator.start();
    }

    private void startTranslate() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
//            scale = scale - (scale * value * 0.02f);

            x = (int) (startX + (endX - startX) * value);
            y = (int) (startY + (endY - startY) * value);

        });

        animator.setDuration(800);
        animator.start();
    }
}

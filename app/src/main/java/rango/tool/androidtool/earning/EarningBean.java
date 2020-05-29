package rango.tool.androidtool.earning;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

import rango.tool.common.utils.ScreenUtils;

public class EarningBean {

    private int x;
    private int y;

    private int endX;
    private int endY;

    private int startX;
    private int startY;

    private float scale;

    private int rotation;

    private TimeInterpolator interpolator;

    private int[] firstRotationArray;
    private long firstRotationDelay;
    private int firstDisX;
    private int firstDisY;
    private float[] firstScaleArray;

    private long firstDuration;
    private long firstRotationDuration;

    private float[] secondScaleArray;
    private long secondDuration;

    private float thirdEndScale;
    private int thirdDisRotation;
    private long thirdDuration;
    private long thirdDelay;

    EarningBean(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        x = startX;
        y = startY;
        scale = 0f;
    }

    void setFirstTranslation(int disX, int disY) {
        firstDisX = ScreenUtils.dp2px(disX);
        firstDisY = ScreenUtils.dp2px(disY);
    }

    void setFirstScaleArray(float[] value) {
        firstScaleArray = value;
    }

    void setInterpolator(TimeInterpolator value) {
        this.interpolator = value;
    }

    void setFirstRotationArray(int[] value) {
        firstRotationArray = value;
    }

    void setFirstRotationDelay(long delay) {
        firstRotationDelay = delay;
    }

    void setSecondScaleArray(float[] value) {
        secondScaleArray = value;
    }

    void setSecondDuration(long duration) {
        secondDuration = duration;
    }

    void setThirdEndScale(float scale) {
        this.thirdEndScale = scale;
    }

    void setThirdDisRotation(int value) {
        thirdDisRotation = value;
    }

    void setThirdDuration(long duration) {
        thirdDuration = duration;
    }

    void setThirdDelay(long delay) {
        thirdDelay = delay;
    }

    float getScale() {
        return scale;
    }

    float getRotation() {
        return rotation;
    }

    void setFirstDuration(long firstDuration) {
        this.firstDuration = firstDuration;
    }

    void setFirstRotationDuration(long duration) {
        this.firstRotationDuration = duration;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public void start(long delay) {

        startFirstScaleAndTranslate(delay);

        startFirstRotation();
    }

    private void startFirstScaleAndTranslate(long delay) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        if (interpolator != null) {
            animator.setInterpolator(interpolator);
        }
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            scale = firstScaleArray[0] + (firstScaleArray[1] - firstScaleArray[0]) * value;
            x = (int) (startX + firstDisX * value);
            y = (int) (startY + firstDisY * value);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startSecondScale();
            }
        });
        animator.setDuration(firstDuration);
        animator.setStartDelay(delay);
        animator.start();
    }

    private void startFirstRotation() {
        ValueAnimator animator = ValueAnimator.ofInt(firstRotationArray);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> rotation = (int) animation.getAnimatedValue());
        animator.setDuration(firstRotationDuration);
        animator.setStartDelay(firstRotationDelay);
        animator.start();
    }

    private void startSecondScale() {
        ValueAnimator animator = ValueAnimator.ofFloat(secondScaleArray);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> scale = (float) animation.getAnimatedValue());
        animator.setDuration(secondDuration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startThirdAnim();
            }
        });
        animator.start();
    }

    private float thirdStartScale;
    private int thirdStartX;
    private int thirdStartY;
    private int thirdStartRotation;

    private void startThirdAnim() {
        thirdStartScale = scale;
        thirdStartX = x;
        thirdStartY = y;
        thirdStartRotation = rotation;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            scale = thirdStartScale + (thirdEndScale - thirdStartScale) * value;
            x = (int) (thirdStartX + (endX - thirdStartX) * value);
            y = (int) (thirdStartY + (endY - thirdStartY) * value);
            rotation = (int) (thirdStartRotation + thirdDisRotation * value);
        });

        animator.setDuration(thirdDuration);
        animator.setStartDelay(thirdDelay);
        animator.start();
    }
}

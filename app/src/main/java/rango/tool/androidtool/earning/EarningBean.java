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

    private int[] firstRotateArray;
    private long firstRotateDelay;
    private int firstDisX;
    private int firstDisY;
    private float[] firstScaleArray;
    private long firstScaleAndTranslateDuration;
    private long firstRotateDuration;

    private float[] secondScaleArray;
    private long secondDuration;

    private float lastEndScale;
    private int lastDisRotate;
    private long lastDuration;
    private long lastDelay;

    float getScale() {
        return scale;
    }

    float getRotation() {
        return rotation;
    }


    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public void start(long delay) {
        reset();

        startFirstScaleAndTranslate(delay);

        startFirstRotation();
    }

    private EarningAnimEndListener earningAnimEndListener;

    void setEarningAnimEndListener(EarningAnimEndListener earningAnimEndListener) {
        this.earningAnimEndListener = earningAnimEndListener;
    }

    private void reset() {
        x = startX;
        y = startY;
        scale = 1f;
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
        animator.setDuration(firstScaleAndTranslateDuration);
        animator.setStartDelay(delay);
        animator.start();
    }

    private void startFirstRotation() {
        ValueAnimator animator = ValueAnimator.ofInt(firstRotateArray);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> rotation = (int) animation.getAnimatedValue());
        animator.setDuration(firstRotateDuration);
        animator.setStartDelay(firstRotateDelay);
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
                startLastAnim();
            }
        });
        animator.start();
    }

    private float lastStartScale;
    private int lastStartX;
    private int lastStartY;
    private int lastStartRotation;

    private void startLastAnim() {
        lastStartScale = scale;
        lastStartX = x;
        lastStartY = y;
        lastStartRotation = rotation;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            scale = lastStartScale + (lastEndScale - lastStartScale) * value;
            x = (int) (lastStartX + (endX - lastStartX) * value);
            y = (int) (lastStartY + (endY - lastStartY) * value);
            rotation = (int) (lastStartRotation + lastDisRotate * value);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scale = 0;
                if (earningAnimEndListener != null) {
                    earningAnimEndListener.onAnimEnd();
                }
            }
        });
        animator.setDuration(lastDuration);
        animator.setStartDelay(lastDelay);
        animator.start();
    }

    @SuppressWarnings("SameParameterValue")
    public static final class Builder {
        private int endX;
        private int endY;

        private int startX;
        private int startY;

        private TimeInterpolator interpolator;

        private int[] firstRotateArray;
        private long firstRotateDelay;
        private int firstDisX;
        private int firstDisY;
        private float[] firstScaleArray;

        private long firstScaleAndTranslateDuration;
        private long firstRotateDuration;

        private float[] secondScaleArray;
        private long secondDuration;

        private float lastEndScale;
        private int lastDisRotate;
        private long lastDuration;
        private long lastDelay;

        Builder setStartPointer(int startX, int startY) {
            this.startX = startX;
            this.startY = startY;
            return this;
        }

        Builder setEndPointer(int endX, int endY) {
            this.endX = endX;
            this.endY = endY;
            return this;
        }

        Builder setInterpolator(TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        Builder setFirstAnim(int[] firstRotateArray, long firstRotateDelay, long firstRotateDuration, int firstDisX, int firstDisY, float[] firstScaleArray, long firstScaleAndTranslateDuration) {
            this.firstRotateArray = firstRotateArray;
            this.firstRotateDelay = firstRotateDelay;
            this.firstRotateDuration = firstRotateDuration;

            this.firstDisX = ScreenUtils.dp2px(firstDisX);
            this.firstDisY = ScreenUtils.dp2px(firstDisY);
            this.firstScaleArray = firstScaleArray;
            this.firstScaleAndTranslateDuration = firstScaleAndTranslateDuration;
            return this;
        }

        Builder setSecondAnim(float[] secondScaleArray, long secondDuration) {
            this.secondScaleArray = secondScaleArray;
            this.secondDuration = secondDuration;
            return this;
        }

        Builder setLastAnim(float lastEndScale, int lastDisRotate, long lastDuration, long lastDelay) {
            this.lastEndScale = lastEndScale;
            this.lastDisRotate = lastDisRotate;
            this.lastDuration = lastDuration;
            this.lastDelay = lastDelay;
            return this;
        }

        public EarningBean build() {
            EarningBean bean = new EarningBean();
            bean.endX = endX;
            bean.endY = endY;
            bean.startX = startX;
            bean.startY = startY;
            bean.interpolator = interpolator;

            bean.firstRotateDelay = firstRotateDelay;
            bean.firstRotateDuration = firstRotateDuration;
            bean.firstDisY = firstDisY;
            bean.firstDisX = firstDisX;
            bean.firstScaleArray = firstScaleArray;
            bean.firstRotateArray = firstRotateArray;
            bean.firstScaleAndTranslateDuration = firstScaleAndTranslateDuration;

            bean.secondDuration = secondDuration;
            bean.secondScaleArray = secondScaleArray;

            bean.lastDelay = lastDelay;
            bean.lastDisRotate = lastDisRotate;
            bean.lastDuration = lastDuration;
            bean.lastEndScale = lastEndScale;

            return bean;
        }
    }
}

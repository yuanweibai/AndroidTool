package rango.tool.androidtool.clickeffect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import rango.tool.common.utils.ScreenUtils;

public class RippleTextView extends AppCompatTextView {

    private final int MIN_WIDTH = ScreenUtils.dp2px(30);
    private final int EXTRA_DISTANCE = 700;
    private final int[] INIT_COLORS = new int[]{Color.WHITE, Color.RED, Color.RED, Color.WHITE};
    private final int[] END_COLORS = new int[]{Color.RED, Color.RED};

    private Paint paint;
    private int radius;
    private int[] colorArray;
    private boolean isStartUpAnim = false;
    private int alpha = 255;

    private float startX = -1;
    private float endX = -1;
    private ValueAnimator upAnimator;
    private ValueAnimator downAnimator;

    public RippleTextView(Context context) {
        this(context, null);
    }

    public RippleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        radius = ScreenUtils.dp2px(12);
        colorArray = INIT_COLORS;
        setClickable(true);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (isStartUpAnim) {
            paint.setAlpha(255);
            paint.setColor(Color.WHITE);
            paint.setShader(null);
            RectF rectF = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rectF, radius, radius, paint);

            paint.setAlpha(alpha);
            LinearGradient linearGradient = new LinearGradient(startX, 0, endX, 0, colorArray, null, Shader.TileMode.CLAMP);
            paint.setShader(linearGradient);
            RectF rf = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rf, radius, radius, paint);

        } else {
            paint.setColor(Color.WHITE);
            if (startX != -1 && endX != -1) {
                LinearGradient linearGradient = new LinearGradient(startX, 0, endX, 0, colorArray, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
            } else {
                paint.setShader(null);
            }
            RectF rectF = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int pointerId = event.getPointerId(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = getPointerX(event, pointerId);
                if (downX != -1) {
                    startDownAnim(downX, getMeasuredWidth());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startUpAnim();
                break;
        }
        return super.onTouchEvent(event);
    }

    private float getPointerX(MotionEvent event, int pointerId) {
        int index = event.findPointerIndex(pointerId);
        if (index < 0) {
            return -1;
        }
        return event.getX(index);
    }

    private float getPointerY(MotionEvent event, int pointerId) {
        int index = event.findPointerIndex(pointerId);
        if (index < 0) {
            return -1;
        }
        return event.getY(index);
    }

    private void startDownAnim(float downX, int width) {
        isStartUpAnim = false;

        if (downAnimator != null && downAnimator.isRunning()) {
            downAnimator.cancel();
        }
        if (upAnimator != null && upAnimator.isRunning()) {
            upAnimator.cancel();
        }

        downAnimator = ValueAnimator.ofFloat(downX, 0);
        downAnimator.addUpdateListener(animation -> {
            float x = (float) animation.getAnimatedValue();
            drawBg(downX, x, width);
        });

        downAnimator.addListener(new AnimatorListenerAdapter() {

            private boolean isCancel = false;

            @Override
            public void onAnimationStart(Animator animation) {
                colorArray = INIT_COLORS;
                drawBg(downX, downX, width);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCancel) {
                    colorArray = END_COLORS;
                    invalidate();
                }
            }
        });

        downAnimator.setDuration(200);
        downAnimator.start();
    }

    private void drawBg(float downX, float x, int width) {
        float ratio = (downX - x) / downX;
        startX = downX - MIN_WIDTH / 2f - ratio * (downX + EXTRA_DISTANCE);
        endX = downX + MIN_WIDTH / 2f + ratio * (width - downX + EXTRA_DISTANCE);
        invalidate();
    }

    private void startUpAnim() {
        isStartUpAnim = true;
        if (downAnimator != null && downAnimator.isRunning()) {
            downAnimator.cancel();
        }
        if (upAnimator != null && upAnimator.isRunning()) {
            upAnimator.cancel();
        }
        upAnimator = ValueAnimator.ofInt(255, 0);
        upAnimator.addUpdateListener(animation -> {
            alpha = (int) animation.getAnimatedValue();
            invalidate();
        });

        upAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                alpha = 0;
                invalidate();
            }
        });

        upAnimator.setDuration(200);
        upAnimator.start();
    }
}

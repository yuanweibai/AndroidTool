package rango.tool.androidtool.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class WaveButton extends AppCompatButton {

    private static final String TAG = WaveButton.class.getSimpleName();
    private static Interpolator pathInterpolatorCompat;
    private final int DEFAULT_REPEAT_COUNT = 5;
    private final int DEFAULT_DURATION = 1000;
    private float waveRadiusWidth;
    private float waveRadiusHeight;
    private float maxWaveRadiusWidth;
    private float maxWaveRadiusHeight;
    private Paint paint;
    private RectF rectF;
    private int width;
    private int height;
    private float initRadiusWidth;
    private float initRadiusHeight;
    private boolean isAnimRunning;
    private ValueAnimator valueAnimator;
    private int animCount = 1;
    private float lastValue;

    {
        pathInterpolatorCompat = PathInterpolatorCompat.create(0.36f, 0.57f, 0.52f, 1f);
    }

    public WaveButton(Context context) {
        this(context, null);
    }

    public WaveButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getResources().getColor(R.color.main_pressed));
        post(new Runnable() {
            @Override
            public void run() {
                startWave();
            }
        });
    }

    private int calculateAlpha(float value) {
        int alpha;
        if (value < 0.2f) {
            alpha = (int) (6375 * value * value);
        } else {
            alpha = (int) ((1 - value) * 255);
        }

        return alpha;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        stop();
        return super.onTouchEvent(event);
    }

    private void initSize() {
        if (initRadiusWidth > 0) {
            return;
        }
        initRadiusWidth = ScreenUtils.getTextWidth(String.valueOf(getText()), getPaint()) / 2 + ScreenUtils.dp2px(48);
        float ratio = initRadiusWidth / maxWaveRadiusWidth;
        initRadiusHeight = maxWaveRadiusHeight * ratio;
    }

    public void stop() {
        if (isAnimRunning) {
            valueAnimator.cancel();
        }
    }

    private float calculateValue(float value) {
        Log.e(TAG, "value: " + value);
//        return (float) Math.sqrt(value);
//        return value;
        float result;
//        float a = 50 / 21f;
//        float b = 1f - a;

        if (value <= 0.2f) {
            float a = 1f;
            float b = -0.1f;
            result = (float) (Math.sqrt(b * b + 4 * a * value) - b) / (2 * a);
            lastValue = result;
        } else {
            float a = 1f;
            float b = 0.1f;
            float c = -0.1f;
            result = (float) (Math.sqrt(b * b - 4 * a * (c - value)) - b) / (2 * a);
//            result = lastValue;
        }


//        }

//        if (value > 0.8f) {
//            result = (float) Math.log(value + 1);
//        }
//        float result = (float) Math.log(value + 1);
        Log.e(TAG, "result value: " + result);


        return result;
    }

    public void startWave() {
        if (isAnimRunning) {
            return;
        }
        animCount = 1;
        initSize();
        initWaveAnim();
        valueAnimator.start();
    }

    private void initWaveAnim() {
        if (valueAnimator != null) {
            return;
        }
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
//                float value = calculateValue(v);
                paint.setAlpha(calculateAlpha(value));
                waveRadiusWidth = initRadiusWidth + (maxWaveRadiusWidth - initRadiusWidth) * value;
                waveRadiusHeight = initRadiusHeight + (maxWaveRadiusHeight - initRadiusHeight) * value;
                invalidate();
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "onAnimationEnd");
                isAnimRunning = false;
                waveRadiusHeight = 0;
                waveRadiusWidth = 0;
                invalidate();
                if (animCount < DEFAULT_REPEAT_COUNT) {
                    animCount++;
                    valueAnimator.setStartDelay(200);
                    valueAnimator.start();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Log.e(TAG, "onAnimationStart");
                isAnimRunning = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimRunning = false;
                Log.e(TAG, "onAnimationCancel");
                super.onAnimationCancel(animation);
            }
        });

        valueAnimator.setInterpolator(pathInterpolatorCompat);
        valueAnimator.setDuration(DEFAULT_DURATION);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        maxWaveRadiusWidth = width / 2f * 1.1f;
        maxWaveRadiusHeight = height / 2f * 7f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        float left = width / 2f - waveRadiusWidth;
        float top = height / 2f - waveRadiusHeight;
        float right = width / 2f + waveRadiusWidth;
        float bottom = height / 2f + waveRadiusHeight;
        if (rectF == null) {
            rectF = new RectF(left, top, right, bottom);
        } else {
            rectF.set(left, top, right, bottom);
        }
        canvas.drawOval(rectF, paint);
        canvas.restore();
        super.onDraw(canvas);

    }
}

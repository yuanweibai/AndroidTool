package rango.tool.androidtool.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import rango.tool.common.utils.ScreenUtils;

public class CountDownTextView extends AppCompatTextView {

    private Paint paint;
    private int strokeWidth;
    private final int startAngle = -90;
    private int angle;

    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokeWidth = ScreenUtils.dp2px(12);
    }

    @Override protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = Math.min(width, height) / 2 - strokeWidth / 2;
        int cx = width / 2;
        int cy = height / 2;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        drawFirstRing(canvas, cx, cy, radius);
        drawSecondRing(canvas, cx, cy, radius);
        super.onDraw(canvas);
    }

    private void drawFirstRing(Canvas canvas, int cx, int cy, int r) {
        paint.setColor(Color.RED);
        canvas.drawCircle(cx, cy, r, paint);
    }

    private void drawSecondRing(Canvas canvas, int cx, int cy, int r) {
        paint.setColor(Color.WHITE);
        RectF rectF = new RectF(cx - r, cy - r, cx + r, cy + r);
        canvas.drawArc(rectF, startAngle, -angle, false, paint);
    }

    public void startCountDown(int num) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(num, 0);
        valueAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            angle = (int) (((num - value) / (float) num) * 360);
            setText(String.valueOf((int) Math.ceil(value)));
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                angle = 360;
                setText("0");
            }
        });
        valueAnimator.setDuration(num * 1000);
        valueAnimator.start();
    }
}

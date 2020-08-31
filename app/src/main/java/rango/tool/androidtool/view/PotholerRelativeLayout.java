package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import rango.tool.common.utils.ScreenUtils;
import rango.tool.common.utils.TouchEventUtils;

public class PotholerRelativeLayout extends RelativeLayout {

    private Paint potholerPaint;
    private @ColorRes
    int colorRes;
    private static final float DEFAULT_OFFSET = ScreenUtils.dp2px(8);
    private static final float DEFAULT_RADIUS = ScreenUtils.dp2px(6);

    private boolean isInterceptTouchEvent = true;
    private List<List<RectF>> rectFListList;
    private List<RectF> currentRectFList;
    private int currentIndex = 0;

    public PotholerRelativeLayout(Context context) {
        this(context, null);
    }

    public PotholerRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PotholerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        potholerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        potholerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public void setRectF(@ColorRes int colorRes, List<List<RectF>> rectFList) {
        this.colorRes = colorRes;
        this.rectFListList = rectFList;
        currentIndex = 0;
    }

    public void showNext() {
        if (rectFListList == null || rectFListList.isEmpty()) {
            return;
        }
        currentIndex++;
        if (currentIndex >= rectFListList.size()) {
            return;
        }
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (rectFListList != null && !rectFListList.isEmpty()) {
            canvas.drawColor(getResources().getColor(colorRes));

            List<RectF> rectFS = rectFListList.get(currentIndex);
            if (currentRectFList == null) {
                currentRectFList = new ArrayList<>();
            } else {
                currentRectFList.clear();
            }
            for (RectF rectF : rectFS) {
                RectF currentRectF = initCurrentRectF(rectF);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(currentRectF,
                            DEFAULT_RADIUS, DEFAULT_RADIUS, potholerPaint);
                } else {
                    canvas.drawRect(currentRectF, potholerPaint);
                }
                currentRectFList.add(currentRectF);
            }

        }
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int pointerId = TouchEventUtils.getDefaultPointerId(event);
        if (action == MotionEvent.ACTION_DOWN) {
            float x = TouchEventUtils.getX(event, pointerId);
            float y = TouchEventUtils.getY(event, pointerId);

            isInterceptTouchEvent = true;
            for (RectF rectF : currentRectFList) {
                if (rectF.contains(x, y)) {
                    isInterceptTouchEvent = false;
                    break;
                }
            }
        }
        return isInterceptTouchEvent;
    }

    private RectF initCurrentRectF(RectF rectF) {
        return new RectF(rectF.left - DEFAULT_OFFSET,
                rectF.top - DEFAULT_OFFSET,
                rectF.right + DEFAULT_OFFSET,
                rectF.bottom + DEFAULT_OFFSET);
    }
}

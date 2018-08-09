package rango.tool.androidtool.clickeffect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.Queue;

public class RippleRelativeLayout extends RelativeLayout {

    private RippleView currentRippleView;

    private Queue<RippleView> queue = new LinkedList<>();

    public RippleRelativeLayout(Context context) {
        this(context, null);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        initView(context);
    }

    private void initView(Context context) {
        RippleView firstRippleView = new RippleView(context);
        RippleView secondRippleView = new RippleView(context);

        queue.add(firstRippleView);
        queue.add(secondRippleView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int pointerId = event.getPointerId(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = getPointerX(event, pointerId);
                if (downX != -1) {
                    startPressedAnim(downX, getMeasuredWidth());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (currentRippleView != null) {
                    currentRippleView.startUpAnim();
                }
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

    private void startPressedAnim(float downX, int width) {
        RippleView rippleView = queue.poll();
        if (indexOfChild(rippleView) != -1) {
            removeView(rippleView);
        }

        addRippleView(rippleView);
        rippleView.cancelAnim();
        rippleView.startDownAnim(downX, width);
        queue.add(rippleView);
        currentRippleView = rippleView;
    }

    private void addRippleView(View view) {
        int index = 0;
        if (getChildAt(0) instanceof RippleView) {
            index = 1;
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, index, params);
    }
}

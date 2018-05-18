package rango.tool.androidtool.nestedscroll;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NestedScrollChildView extends View implements NestedScrollingChild2 {

    private static final String TAG = NestedScrollChildView.class.getSimpleName();

    private int[] consumed = new int[2];
    private int[] offsetInWindow = new int[2];

    private final NestedScrollingChildHelper childHelper;

    public NestedScrollChildView(Context context) {
        this(context, null);
    }

    public NestedScrollChildView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        childHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    private float downY;
    private float downX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int pointerId = event.getPointerId(0);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = getPointerY(event, pointerId);
                downX = getPointerX(event, pointerId);
                if (downY == -1 || downX == -1) {
                    return false;
                }
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL | ViewCompat.SCROLL_AXIS_HORIZONTAL, ViewCompat.TYPE_TOUCH);
                break;
            case MotionEvent.ACTION_MOVE:
                final float pointerY = getPointerY(event, pointerId);
                final float pointerX = getPointerX(event, pointerId);

                if (pointerY == -1 || pointerX == -1) {
                    return false;
                }
                float deltaY = pointerY - downY;
                float deltaX = pointerX - downX;

                Log.e(TAG, String.format("downY = %f", downY));
                Log.e(TAG, String.format("downX = %f", downX));
                Log.e(TAG, String.format("before dispatchNestedPreScroll, deltaY = %f", deltaY));
                Log.e(TAG, String.format("before dispatchNestedPreScroll, deltaX = %f", deltaX));
                if (dispatchNestedPreScroll((int) deltaX, (int) deltaY, consumed, offsetInWindow, ViewCompat.TYPE_TOUCH)) {
                    deltaY -= consumed[1];
                    deltaX -= consumed[0];
                    Log.e(TAG, String.format("after dispatchNestedPreScroll, deltaY = %f", deltaY));
                    Log.e(TAG, String.format("after dispatchNestedPreScroll, deltaX = %f", deltaX));
                }
                if (Math.floor(Math.abs(deltaY)) == 0) {
                    deltaY = 0;
                }

                if (Math.floor(Math.abs(deltaX)) == 0) {
                    deltaX = 0;
                }
                View parent = (View) getParent();
                setY(MathUtils.clamp(getY() + deltaY, 0, (parent.getHeight() - getHeight())));
                setX(MathUtils.clamp(getX() + deltaX, 0, (parent.getWidth() - getWidth())));
                break;
        }
        return true;
    }

    private float getPointerY(MotionEvent event, int pointerId) {
        final int pointerIndex = event.findPointerIndex(pointerId);
        if (pointerIndex < 0) {
            return -1;
        }
        return event.getY(pointerIndex);
    }

    private float getPointerX(MotionEvent event, int pointerId) {
        final int pointerIndex = event.findPointerIndex(pointerId);
        if (pointerIndex < 0) {
            return -1;
        }
        return event.getX(pointerIndex);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.e(TAG, String.format("setNestedScrollingEnabled , enabled = %b", enabled));
        childHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        Log.e(TAG, String.format("isNestedScrollingEnabled"));
        return childHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        Log.e(TAG, String.format("startNestedScroll, axes = %d", axes));
        return childHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        Log.e(TAG, String.format("StopNestedScroll"));
        childHelper.stopNestedScroll(type);
    }

    @Override public boolean hasNestedScrollingParent(int type) {
        Log.e(TAG, "hasNestedScrollingParent");
        return childHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        final boolean b = childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        Log.e(TAG, String.format("dispatchNestedScroll , dxConsumed = %d , dyConsumed = %d, dxUnconsumed = %d, dyUnconsumed = %d, offsetInWindow = %s,", dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow));
        return b;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        final boolean b = childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
        Log.e(TAG, String.format("dispatchNestedPreScroll, dx = %d,dy = %d,consumed = %s, offsetInWindow = %s", dx, dy, consumed, offsetInWindow));
        return b;
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, String.format("dispatchNestedFling, velocityX = %f, velocityY = %f,consumed = %b", velocityX, velocityY, consumed));
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Log.e(TAG, String.format("dispatchNestedPreFling, velocityX = %f, velocityY = %f", velocityX, velocityY));
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}

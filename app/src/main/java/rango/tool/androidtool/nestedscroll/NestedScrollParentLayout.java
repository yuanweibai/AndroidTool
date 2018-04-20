package rango.tool.androidtool.nestedscroll;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class NestedScrollParentLayout extends RelativeLayout implements NestedScrollingParent {

    private static final String TAG = NestedScrollParentLayout.class.getSimpleName();

    private NestedScrollingParentHelper parentHelper;

    public NestedScrollParentLayout(Context context) {
        this(context, null);
    }

    public NestedScrollParentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, String.format("onStartNestedScroll, child = %s, target = %s, nestedScrollAxes = %d", child, target, nestedScrollAxes));
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.e(TAG, String.format("onNestedScrollAccepted, child = %s, target = %s,axes = %d", child, target, axes));
        parentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.e(TAG, "onStopNestedScroll");
        parentHelper.onStopNestedScroll(child);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, String.format("onNestedScroll, target = %s,dxConsumed = %d,dyConsumed = %d,dxUnconsumed = %d,dyUnconsumed = %d", target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed));
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        final float shouldMoveY = getY() + dy;
        final View parent = (View) getParent();
        int consumedY;
        if (shouldMoveY <= 0) {
            consumedY = -(int) getY();
        } else if (shouldMoveY >= parent.getHeight() - getHeight()) {
            consumedY = (int) (parent.getHeight() - getHeight() - getY());
        } else {
            consumedY = dy;
        }
        setY(getY() + consumedY);
        consumed[1] = consumedY;
        Log.e(TAG, String.format("onNestedPreScroll, dx = %d,dy = %d,consumed = %s", dx, dy, consumed));
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, String.format("onNestedFling,velocityX = %f,velocityY = %f,consumed = %b", velocityX, velocityY, consumed));
        return true;
    }

    @Override public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, String.format("onNestedPreScroll, velocityX = %f,velocityY = %f", velocityX, velocityY));
        return true;
    }

    @Override public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAces");
        return parentHelper.getNestedScrollAxes();
    }
}

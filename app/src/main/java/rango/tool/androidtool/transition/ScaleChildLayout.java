package rango.tool.androidtool.transition;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

public class ScaleChildLayout extends RelativeLayout {

    private static final String TAG = ScaleChildLayout.class.getSimpleName();
    private int width;
    private int height;
    private int touchSlop;

    private float downX;
    private float downY;
    private float lastMoveX;
    private float lastMoveY;
    private float moveX;
    private float moveY;
    private OnTouchEventListener onTouchEventListener;

    public ScaleChildLayout(Context context) {
        this(context, null);
    }

    public ScaleChildLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleChildLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        touchSlop = ViewConfiguration.getTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        width = getWidth();
        height = getHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                lastMoveX = downX;
                lastMoveY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getRawX() - lastMoveX > touchSlop || ev.getRawY() - lastMoveY > touchSlop) {
                    return true;
                }
                lastMoveX = ev.getRawX();
                lastMoveY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (ev.getRawY() - downY > touchSlop || ev.getRawX() - downX > touchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();

                lastMoveX = downX;
                lastMoveY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();
                moveEvent();
                break;
            case MotionEvent.ACTION_UP:
                upEvent();
                break;
            default:
                break;
        }

        return true;
    }

    private void moveEvent() {
        moveScaleChild();
        moveTransitionChild();
    }

    private void upEvent() {
        if (onTouchEventListener != null) {
            onTouchEventListener.up();
        }
    }

    private void moveScaleChild() {
        float scaleX = (1 - (moveX - downX) / (width - downX));
        float scaleY = (1 - (moveY - downY) / (height - downY));
        scaleChild(scaleY, scaleY);
    }

    private void moveTransitionChild() {
        int transitionX = (int) (moveX - lastMoveX);
        int transitionY = (int) (moveY - lastMoveY);
        transitionChild(transitionX, transitionY);
    }

    private void scaleChild(float scaleX, float scaleY) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setScaleX(scaleX);
            child.setScaleY(scaleY);
        }
    }

    private void transitionChild(int transitionX, int transitionY) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setTranslationX(transitionX);
            child.setTranslationY(transitionY);
        }
    }

    public void setOnTouchEventListener(OnTouchEventListener listener) {
        onTouchEventListener = listener;
    }

    public interface OnTouchEventListener {
        void up();
    }
}

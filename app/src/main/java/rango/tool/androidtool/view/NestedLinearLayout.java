package rango.tool.androidtool.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import rango.tool.androidtool.R;
import rango.tool.androidtool.gesture.ScrollGestureDetectorListener;
import rango.tool.androidtool.gesture.ToolGestureDetector;

public class NestedLinearLayout extends LinearLayout {

    private RecyclerView recyclerView;

    private ToolGestureDetector detector;

    public NestedLinearLayout(Context context) {
        this(context, null, 0);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        detector = new ToolGestureDetector(context, new ScrollGestureDetectorListener(this));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private float lastX;
    private float lastY;
    private float downX;
    private float downY;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("rango", "onInterceptTouchEvent()   action: " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                lastX = downX;
                lastY = downY;
                detector.prefromOnDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float x = ev.getRawX();
                float y = ev.getRawY();
                if ((isTop() && y > lastY) || getTranslationY() != 0) {
                    lastX = x;
                    lastY = y;
                    return true;
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (getTranslationY() != 0) {
                    return true;
                }
                break;

        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("rango", "onTouchEvent()   action: " + event.getAction());
        return detector.onTouchEvent(event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                float x = event.getRawX();
//                float y = event.getRawY();
//                moveView(x, y);
//                break;
//            case MotionEvent.ACTION_UP:
//                setTranslationY(0);
//                break;
//        }
//        return true;
    }

    private boolean isTop() {
        return ((LinearLayoutManager) (recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() == 0;
    }

    private void moveView(float x, float y) {
        float currentTranslationY = getTranslationY() + (y - downY);
        setTranslationY(currentTranslationY);
        downX = x;
        downY = y;

    }
}

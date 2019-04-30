package rango.tool.androidtool.gesture;

import android.util.Log;
import android.view.MotionEvent;

import rango.tool.androidtool.view.NestedLinearLayout;

public class ScrollGestureDetectorListener extends IGestureDetectorListener {

    private NestedLinearLayout nestedView;

    private float lastX;
    private float lastY;


    public ScrollGestureDetectorListener(NestedLinearLayout nestedView) {
        this.nestedView = nestedView;
    }

    @Override
    boolean onActionUp(MotionEvent e) {
        setTranslationY(0);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        lastX = e.getRawX();
        lastY = e.getRawY();
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float x = e2.getRawX();
        float y = e2.getRawY();
        moveView(x, y);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("rango", "onFling: velocityX = " + velocityX + ", velocityY = " + velocityY);
        setTranslationY(0);
        return true;
    }

    private void setTranslationY(float y) {
        nestedView.setTranslationY(y);
    }

    private float getTranslationY() {
        return nestedView.getTranslationY();
    }

    private void moveView(float x, float y) {
        float currentTranslationY = getTranslationY() + (y - lastY);
        setTranslationY(currentTranslationY);
        lastX = x;
        lastY = y;
    }

}

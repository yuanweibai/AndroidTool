package rango.tool.androidtool.surfaceview.lib;

import android.graphics.Canvas;

public interface IDrawableBean {

    void start();

    void onDraw(Canvas canvas);

    void stop();

    boolean isInterceptTouchEvent();

    boolean onActionDown(float downX, float downY);

    void onActionMove(float disX, float disY);

    void onActionUp(float upX, float upY, float disX, float disY);
}

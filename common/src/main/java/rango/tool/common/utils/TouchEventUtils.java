package rango.tool.common.utils;

import android.view.MotionEvent;

public class TouchEventUtils {


    public static int getDefaultPointerId(MotionEvent event) {
        return event.getPointerId(0);
    }

    public static float getY(MotionEvent event, int pointerId) {
        final int pointerIndex = event.findPointerIndex(pointerId);
        if (pointerIndex < 0) {
            return -1;
        }

        return event.getY(pointerIndex);
    }

    public static float getX(MotionEvent event, int pointerId) {
        final int pointerIndex = event.findPointerIndex(pointerId);
        if (pointerIndex < 0) {
            return -1;
        }
        return event.getX(pointerIndex);
    }
}

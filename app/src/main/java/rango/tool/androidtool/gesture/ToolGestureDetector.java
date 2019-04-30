package rango.tool.androidtool.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class ToolGestureDetector extends GestureDetector {

    private IGestureDetectorListener mGestureDetectorListener;

    public ToolGestureDetector(Context context, IGestureDetectorListener listener) {
        super(context, listener);
        mGestureDetectorListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);

        if (!result) {
            if ((ev.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                if (mGestureDetectorListener != null) {
                    return mGestureDetectorListener.onActionUp(ev);
                }
            }
        }
        return result;
    }

    public void prefromOnDown(MotionEvent event) {
        if (mGestureDetectorListener != null) {
            mGestureDetectorListener.onDown(event);
        }
    }
}

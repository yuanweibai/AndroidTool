package rango.tool.androidtool.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class TestTouchLayout extends RelativeLayout {
    public TestTouchLayout(Context context) {
        super(context);
    }

    public TestTouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTouchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        Log.e("TestTouchLayout", "event: " + event.getAction());
        boolean result = super.onTouchEvent(event);
        Log.e("TestTouchLayout","result = "+result);
        return result;
    }
}

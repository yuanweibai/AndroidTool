package rango.tool.androidtool.touch;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class MyImageView extends AppCompatImageView {

    private static final String TAG = MyImageView.class.getSimpleName();

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e(TAG, "ACTION_DOWN");
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "ACTION_MOVE");
//                return true;
//            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "ACTION_UP");
//                return false;
//            default:
//                Log.e(TAG, "DEFAULT");
//                return true;
//        }
//    }
}

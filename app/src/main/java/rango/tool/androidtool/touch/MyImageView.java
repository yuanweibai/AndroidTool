package rango.tool.androidtool.touch;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyImageView extends AppCompatImageView {

    private static final String TAG = MyImageView.class.getSimpleName();

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


//        setClickable(true);

//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.e(TAG, "ACTION_DOWN");
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.e(TAG, "ACTION_MOVE");
//                        return false;
//                    case MotionEvent.ACTION_UP:
//                        Log.e(TAG, "ACTION_UP");
//                        return false;
//                }
//                return false;
//            }
//        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return true;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "ACTION_DOWN");
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "ACTION_MOVE");
                return false;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ACTION_UP");
                return false;
            default:
                Log.e(TAG, "DEFAULT: action = " + event.getAction());
                return false;
        }
    }
}

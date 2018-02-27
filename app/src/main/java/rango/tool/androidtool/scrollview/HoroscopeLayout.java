package rango.tool.androidtool.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class HoroscopeLayout extends RelativeLayout {

    private final int DEFAULT_DISTANCE = 100;
    private View mTopView;
    private int mTop;
    private int mTotalScrollY;

    public HoroscopeLayout(Context context) {
        this(context, null);
    }

    public HoroscopeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoroscopeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTopView(View view) {
        mTopView = view;

    }

    private float mDownX;
    private float mDownY;

    private float mMoveX;
    private float mMoveY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mTop = mTopView.getTop();
//        int top = mTopView.getTop();
//        int scrollY = getScrollY();
//        Log.e("HoroscopeLayout", "top = " + top + ", scrollY = " + scrollY);
//        scrollBy(0, 1);
//        int scrollTop = mTopView.getTop();
//
//        Log.e("HoroscopeLayout", "scrolled top = " + scrollTop + ", scrollY = " + getScrollY());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            default:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getRawX();
                mMoveY = ev.getRawY();
                if ((Math.abs(mMoveY - mDownY) / Math.abs(mMoveX - mDownX)) > 2f && ((mMoveY < mDownY && (mTop - mTotalScrollY) > DEFAULT_DISTANCE) || (mMoveY > mDownY && mTotalScrollY >= 0))) {
                    mDownX = mMoveX;
                    mDownY = mMoveY;
                    return true;
                }


                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getRawX();
                mMoveY = ev.getRawY();
//                if ((Math.abs(mMoveY - mDownY) / Math.abs(mMoveX - mDownX)) > 2f && (mTop - mTotalScrollY) > DEFAULT_DISTANCE) {
                if ((Math.abs(mMoveY - mDownY) / Math.abs(mMoveX - mDownX)) > 2f && ((mMoveY < mDownY && (mTop - mTotalScrollY) > DEFAULT_DISTANCE) || (mMoveY > mDownY && mTotalScrollY >= 0))) {
                    float y = mDownY - mMoveY;
                    float tempScrollY = mTotalScrollY + y;
                    if (tempScrollY > mTop - DEFAULT_DISTANCE) {
                        y = mTop - DEFAULT_DISTANCE - mTotalScrollY;
                    }

                    if (tempScrollY < 0) {
                        y = 0 - mTotalScrollY;
                    }
                    scrollBy(0, (int) y);
                    mTotalScrollY += y;
                    mDownY = mMoveY;
                    mDownX = mMoveX;
                    return true;
                }
                break;
        }

        return false;
    }
}

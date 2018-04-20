package rango.tool.androidtool.coordinator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class HoroscopeLayout extends RelativeLayout {

    public static final int DEFAULT_DISTANCE = 200;
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

    public void setTopDistance(int top) {
        mTop = top;
    }

    private boolean isTop() {
        return true;
    }

    private float mDownX;
    private float mDownY;

    private float mLastMoveX;
    private float mLastMoveY;

    private float mMoveX;
    private float mMoveY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();

                mLastMoveX = mDownX;
                mLastMoveY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getRawX();
                mMoveY = ev.getRawY();
                if ((Math.abs(mMoveY - mLastMoveY) / Math.abs(mMoveX - mLastMoveX)) > 2f && isTop() && ((mMoveY < mLastMoveY && mTotalScrollY >= 0 && (mTop - mTotalScrollY) > DEFAULT_DISTANCE) || (mMoveY > mLastMoveY && mTotalScrollY >= 0))) {
                    mLastMoveX = mMoveX;
                    mLastMoveY = mMoveY;
                    return true;
                }
                break;
            default:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();

                mLastMoveX = mDownX;
                mLastMoveY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getRawX();
                mMoveY = ev.getRawY();
                if ((Math.abs(mMoveY - mLastMoveY) / Math.abs(mMoveX - mLastMoveX)) > 2f && isTop() && ((mMoveY < mLastMoveY && (mTop - mTotalScrollY) > DEFAULT_DISTANCE) || (mMoveY > mLastMoveY && mTotalScrollY > 0))) {
                    float y = mLastMoveY - mMoveY;
                    float tempScrollY = mTotalScrollY + y;
                    if (tempScrollY > mTop - DEFAULT_DISTANCE) {
                        y = mTop - DEFAULT_DISTANCE - mTotalScrollY;
                    } else if (tempScrollY < 0) {
                        y = 0 - mTotalScrollY;
                    }
                    scrollBy(0, (int) y);
                    mTotalScrollY += y;
                    mLastMoveY = mMoveY;
                    mLastMoveX = mMoveX;
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            default:
                float upX = ev.getRawX();
                float upY = ev.getRawY();
                if ((Math.abs(upY - mDownY) / Math.abs(upX - mDownX)) > 2f && mTotalScrollY > 0 && mTotalScrollY < (mTop - DEFAULT_DISTANCE)) {
                    boolean isDown;
                    if (mTotalScrollY < (mTop - DEFAULT_DISTANCE) / 2f) {
                        isDown = true;
                    } else {
                        isDown = false;
                    }

                    endAnim(isDown);
                }
        }

        return super.onTouchEvent(ev);
    }

    private float mLastValue = 0f;

    private void endAnim(boolean isDown) {
        ValueAnimator animator;
        if (isDown) {
            animator = ValueAnimator.ofFloat(mTotalScrollY, 0f);
        } else {
            animator = ValueAnimator.ofFloat(mTotalScrollY, mTop - DEFAULT_DISTANCE);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = animation.getAnimatedFraction();
//                float temp = value - mLastValue;
//
//                float y;
//                if (isDown) {
//                    y = -temp * mTotalScrollY;
//                } else {
//                    y = temp * mTotalScrollY;
//                }
                Log.e("rango", "endAnim() value = " + value + ", mTotalScrollY = " + mTotalScrollY);
//                scrollBy(0, (int) y);
//                mLastValue = value;
            }
        });


        animator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationCancel(Animator animation) {
                if (isDown) {
                    mTotalScrollY = 0;
                } else {
                    mTotalScrollY = mTop - DEFAULT_DISTANCE;
                }
            }

            @Override public void onAnimationEnd(Animator animation) {
                if (isDown) {
                    mTotalScrollY = 0;
                } else {
                    mTotalScrollY = mTop - DEFAULT_DISTANCE;
                }
            }
        });

        animator.setDuration(1000);
        animator.start();
    }
}

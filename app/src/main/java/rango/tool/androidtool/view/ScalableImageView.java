/**
 * created by jiang, 16/6/14
 * Copyright (c) 2016, jyuesong@gmail.com All Rights Reserved.
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScalableImageView extends AppCompatImageView {


    private static final float MIN_POINT_DISTINCT = 10F;
    private Matrix matrix;
    private Matrix cacheMatrix;  //缓存的matrix ，同时记录上一次滑动的位置
    private float mPointDistinct = 1f;
    private float mDegree;
    private float rotate = 0F;// 旋转的角度


    enum Mode {
        NONE, DOWN, MOVE
    }

    private Mode mode; //当前mode
    private Context mContext;

    private PointF mStart = new PointF();
    private PointF mEnd = new PointF();

    public ScalableImageView(Context context) {
        this(context, null);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        matrix = new Matrix();
        cacheMatrix = new Matrix();
        mode = Mode.NONE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                cacheMatrix.set(matrix); //先拷贝一份到缓存
                mode = Mode.DOWN;
                mStart.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointDistinct = calSpacing(event);
                if (mPointDistinct > MIN_POINT_DISTINCT) {
                    cacheMatrix.set(matrix); //先拷贝一份到缓存
                    calPoint(mEnd, event);
                    mode = Mode.MOVE;
                }
                mDegree = calRotation(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //单点触控的时候
                if (mode == Mode.DOWN) {
                    matrix.set(cacheMatrix);
                    matrix.postTranslate(event.getX() - mStart.x, event.getY() - mStart.y);
                } else if (mode == Mode.MOVE && event.getPointerCount() == 2) {  //只能2只手
                    matrix.set(cacheMatrix);
                    float move = calSpacing(event);
                    if (move > MIN_POINT_DISTINCT) {
                        float scale = move / mPointDistinct;
                        matrix.postScale(scale, scale, mEnd.x, mEnd.y);

                    }
                    rotate = calRotation(event);
                    float r = rotate - mDegree;
                    matrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mode = Mode.NONE;
                break;

        }

        setImageMatrix(matrix);
        return true;
    }


    private float calSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void calPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float calRotation(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }

    public void reset() {

        matrix.reset();
        cacheMatrix.reset();
        setImageMatrix(matrix);
        invalidate();
    }


}

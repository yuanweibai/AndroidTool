package rango.tool.androidtool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class TranslationScaleRotationImageView extends RelativeLayout {

    private ImageView imageView;
    private ImageView closeBtn;
    private ImageView scaleRotationBtn;

    public TranslationScaleRotationImageView(Context context) {
        this(context, null, 0);
    }

    public TranslationScaleRotationImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslationScaleRotationImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.sticker_layout, this);
        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.image_view);
        closeBtn = findViewById(R.id.close_btn);
        scaleRotationBtn = findViewById(R.id.scale_rotation_btn);
    }

    private float downX;
    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int downIndex = event.findPointerIndex(0);
                downX = event.getX(downIndex);
                downY = event.getY(downIndex);
                imageView.setRotation(50);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveIndex = event.findPointerIndex(0);
                if (moveIndex != -1) {
                    float moveX = event.getX(moveIndex);
                    float moveY = event.getY(moveIndex);
                    couldTranslate(moveX, moveY);
                    float deltaX = moveX - downX;
                    float deltaY = moveY - downY;
                    translation(deltaX, deltaY);
                    downX = moveX;
                    downY = moveY;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private boolean couldTranslate(float moveX, float moveY) {

        float degree = imageView.getRotation();
        float[] center = getImageViewCenterPoint();
        float de = (float) Math.atan((moveY - center[1]) / moveX - center[0]);
        float realDegree = -degree + de;
        float cos = (float) Math.cos(realDegree);
        float sin = (float) Math.sin(realDegree);
        float dX = Math.abs(moveX - center[0]);
        float dY = Math.abs(moveY - center[1]);
        float diameter = (float) Math.sqrt(dX * dX + dY * dY);
        float x = center[0] + diameter * cos;
        float y = center[1] + diameter * sin;

        float halfWidth = imageView.getWidth() * imageView.getScaleX() / 2f;
        float halfHeight = imageView.getHeight() * imageView.getScaleY() / 2f;
        float left = center[0] - halfWidth;
        float top = center[1] - halfHeight;
        float right = center[0] + halfWidth;
        float bottom = center[1] + halfHeight;

        if (x > left && x < right && y > top && y < bottom) {
            return true;
        } else {
            return false;
        }
    }

    private float[] getImageViewCenterPoint() {
        float[] result = new float[2];
        result[0] = imageView.getLeft() + imageView.getTranslationX() + imageView.getWidth() / 2f;
        result[1] = imageView.getTop() + imageView.getTranslationY() + imageView.getHeight() / 2f + ScreenUtils.getStatusBarHeight();
        return result;
    }

    private void translation(float tx, float ty) {
        imageView.setTranslationX(imageView.getTranslationX() + tx);
        imageView.setTranslationY(imageView.getTranslationY() + ty);
    }

    private void scale(float sx, float sy) {

    }

    private void rotation(float rotation) {

    }
}

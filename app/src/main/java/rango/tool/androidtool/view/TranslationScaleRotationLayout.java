package rango.tool.androidtool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class TranslationScaleRotationLayout extends RelativeLayout {

    private final int MAX_POINTER_COUNT = 2;
    private ImageView imageView;
    private ImageView closeBtn;
    private ImageView scaleRotationBtn;

    public TranslationScaleRotationLayout(Context context) {
        this(context, null, 0);
    }

    public TranslationScaleRotationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslationScaleRotationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.sticker_layout, this);
        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.image_view);
        closeBtn = findViewById(R.id.close_btn);
        scaleRotationBtn = findViewById(R.id.scale_rotation_btn);
    }

    private float[] downX = new float[MAX_POINTER_COUNT];
    private float[] downY = new float[MAX_POINTER_COUNT];
    private boolean[] couldTranslate = new boolean[MAX_POINTER_COUNT];

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int downIndex = event.findPointerIndex(i);
                    if (downIndex != -1) {
                        downX[i] = event.getX(downIndex);
                        downY[i] = event.getY(downIndex);
                        couldTranslate[i] = couldTranslate(downX[i], downY[i]);
                    }

                    if (i == MAX_POINTER_COUNT - 1) {
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int count = event.getPointerCount();
                if (count == 1) {
                    for (int i = 0; i < MAX_POINTER_COUNT; i++) {
                        int moveIndex = event.findPointerIndex(i);
                        if (moveIndex != -1) {
                            float moveX = event.getX(moveIndex);
                            float moveY = event.getY(moveIndex);
                            float disX = moveX - downX[i];
                            float disY = moveY - downY[i];
                            if (couldTranslate[i]) {
                                translation(disX, disY);
                            }
                            downX[i] = moveX;
                            downY[i] = moveY;
                            break;
                        }
                    }
                } else if (count > 1) {
                    int firstMoveIndex = event.findPointerIndex(0);
                    int secondMoveIndex = event.findPointerIndex(1);
                    if (firstMoveIndex != -1 && secondMoveIndex != -1) {
                        float firstMoveX = event.getX(firstMoveIndex);
                        float firstMoveY = event.getY(firstMoveIndex);

                        float secondMoveX = event.getX(secondMoveIndex);
                        float secondMoveY = event.getY(secondMoveIndex);

                        float moveDis = getDistance(firstMoveX, firstMoveY, secondMoveX, secondMoveY);
                        float downDis = getDistance(downX[0], downY[0], downX[1], downY[1]);
                        if (moveDis != 0 && downDis != 0) {
                            float scale = moveDis / downDis;
                            scale(scale);
                        }
                        downX[0] = firstMoveX;
                        downY[0] = firstMoveY;

                        downX[1] = secondMoveX;
                        downY[1] = secondMoveY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        float disX = Math.abs(x2 - x1);
        float disY = Math.abs(y2 - y1);
        return (float) Math.sqrt(disX * disX + disY * disY);
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

        return x > left && x < right && y > top && y < bottom;
    }

    private float[] getImageViewCenterPoint() {
        float[] result = new float[2];
        result[0] = imageView.getLeft() + imageView.getTranslationX() + imageView.getWidth() / 2f;
        result[1] = imageView.getTop() + imageView.getTranslationY() + imageView.getHeight() / 2f + ScreenUtils.getStatusBarHeight();
        return result;
    }

    private void translation(float disX, float disY) {
        imageView.setTranslationX(imageView.getTranslationX() + disX);
        imageView.setTranslationY(imageView.getTranslationY() + disY);
    }

    private void scale(float scale) {
        float oldScale = imageView.getScaleX();
        float newScale = oldScale * scale;
        imageView.setScaleY(newScale);
        imageView.setScaleX(newScale);
    }

    private void rotation(float rotation) {

    }
}

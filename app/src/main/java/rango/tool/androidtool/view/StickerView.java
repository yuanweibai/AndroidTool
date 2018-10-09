package rango.tool.androidtool.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class StickerView extends RelativeLayout {
    private ImageView stickerImageView;
    private ImageView scaleRotationView;
    private ImageView closeBtn;

    public StickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.sticker_layout, this);
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        stickerImageView = findViewById(R.id.image_view);
        scaleRotationView = findViewById(R.id.scale_rotation_btn);
        closeBtn = findViewById(R.id.close_btn);

        stickerImageView.setOnTouchListener(new OnTouchListener() {

            private float mDownX;
            private float mDownY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = event.getRawX();
                        mDownY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();

                        float deltaX = moveX - mDownX;
                        float deltaY = moveY - mDownY;

                        stickerImageView.setTranslationX(stickerImageView.getTranslationX() + deltaX);
                        stickerImageView.setTranslationY(stickerImageView.getTranslationY() + deltaY);

                        scaleRotationView.setTranslationX(scaleRotationView.getTranslationX() + deltaX);
                        scaleRotationView.setTranslationY(scaleRotationView.getTranslationY() + deltaY);

                        closeBtn.setTranslationX(closeBtn.getTranslationX() + deltaX);
                        closeBtn.setTranslationY(closeBtn.getTranslationY() + deltaY);

                        mDownX = moveX;
                        mDownY = moveY;
                        break;
                }
                return true;
            }
        });

        scaleRotationView.setOnTouchListener(new OnTouchListener() {

            private float downX;
            private float downY;

            private float centerX = 0f;
            private float centerY = 0f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initViewCenterXY();
                        downX = event.getRawX();
                        downY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();
                        scaleImage(moveX, moveY);
                        rotationSticker(moveX, moveY);
                        translation(moveX, moveY);

                        downX = moveX;
                        downY = moveY;
                        break;
                }
                return true;
            }

            private void initViewCenterXY() {
                centerX = stickerImageView.getLeft() + stickerImageView.getTranslationX() + stickerImageView.getWidth() / 2f;
                centerY = stickerImageView.getTop() + stickerImageView.getTranslationY() + stickerImageView.getHeight() / 2f + ScreenUtils.getStatusBarHeight();
            }

            private void scaleImage(float moveX, float moveY) {
                float scale = stickerImageView.getScaleX() * (getDistance(moveX, moveY) / getDistance(downX, downY));
                stickerImageView.setScaleY(scale);
                stickerImageView.setScaleX(scale);
            }

            private void rotationSticker(float moveX, float moveY) {
                stickerImageView.setRotation(stickerImageView.getRotation() + getRotateDegree(moveX, moveY));
            }

            private void translation(float moveX, float moveY) {
                scaleRotationView.setTranslationX(scaleRotationView.getTranslationX() + moveX - downX);
                scaleRotationView.setTranslationY(scaleRotationView.getTranslationY() + moveY - downY);

                closeBtn.setTranslationX(closeBtn.getTranslationX() + (downX - moveX));
                closeBtn.setTranslationY(closeBtn.getTranslationY() + (downY - moveY));
            }

            private float getRotateDegree(float moveX, float moveY) {
                double lastRad = Math.atan2(downY - centerY, downX - centerX);
                double currentRad = Math.atan2(moveY - centerY, moveX - centerX);
                double rad = currentRad - lastRad;
                float result = (float) Math.toDegrees(rad);
                Log.e("rotate degree", "result = " + result);
                return result;
            }

            private float getDistance(float x, float y) {
                float tempX = Math.abs(x - centerX);
                float tempY = Math.abs(y - centerY);
                return (float) Math.sqrt(tempX * tempX + tempY * tempY);
            }

            private boolean isInvalid(float moveX, float moveY) {
                int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                return Math.abs(moveX - downX) <= touchSlop && Math.abs(moveY - downY) <= touchSlop;
            }
        });

    }
}

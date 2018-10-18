package rango.tool.androidtool.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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

            private final float MIN_SCALE = 80f / 141f;
            private float downX;
            private float downY;

            private float centerX = 0f;
            private float centerY = 0f;

            private float lastScale = -1f;

            private float minD = 0f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initViewCenterXY();
                        downX = event.getRawX();
                        downY = event.getRawY();
                        float w = MIN_SCALE * stickerImageView.getWidth();
                        float h = MIN_SCALE * stickerImageView.getHeight();
                        minD = (float) (Math.sqrt(w * w + h * h) / 2f);
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();
                        scaleImage(moveX, moveY);
                        rotationSticker(moveX, moveY);

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

                float moveDistance = getDistance(downX, downY);
                if (moveDistance < minD) {
                    return;
                }
                float oldScale = stickerImageView.getScaleX();
                float deltaScale = (getDistance(moveX, moveY) / getDistance(downX, downY));
                float scale = oldScale * deltaScale;

                if (scale < MIN_SCALE) {
                    return;
                }

                stickerImageView.setScaleY(scale);
                stickerImageView.setScaleX(scale);

                float tx = (stickerImageView.getWidth() * (scale - oldScale)) / 2f;
                float ty = (stickerImageView.getHeight() * (scale - oldScale)) / 2f;
                translateEditBtn(tx, ty);
            }

            private void rotationSticker(float moveX, float moveY) {
                float rotation = stickerImageView.getRotation() + getRotateDegree(moveX, moveY);
                float deltaRotation = rotation - stickerImageView.getRotation();
                stickerImageView.setRotation(rotation);

                float halfWidth = stickerImageView.getWidth() * stickerImageView.getScaleX() / 2f;
                float halfHeight = stickerImageView.getHeight() * stickerImageView.getScaleY() / 2f;

                closeBtn.setPivotX(halfWidth + closeBtn.getWidth() / 2f);
                closeBtn.setPivotY(halfHeight + closeBtn.getHeight() / 2f);

                scaleRotationView.setPivotX(-(halfWidth - scaleRotationView.getWidth() / 2f));
                scaleRotationView.setPivotY(-(halfHeight - scaleRotationView.getHeight() / 2f));

                scaleRotationView.setRotation(scaleRotationView.getRotation() + deltaRotation);
                closeBtn.setRotation(closeBtn.getRotation() + deltaRotation);
            }

            private void translateEditBtn(float tx, float ty) {
                scaleRotationView.setTranslationX(scaleRotationView.getTranslationX() + tx);
                scaleRotationView.setTranslationY(scaleRotationView.getTranslationY() + ty);

                closeBtn.setTranslationX(closeBtn.getTranslationX() - tx);
                closeBtn.setTranslationY(closeBtn.getTranslationY() - ty);
            }

            private float getRotateDegree(float moveX, float moveY) {
                double lastRad = Math.atan2(downY - centerY, downX - centerX);
                double currentRad = Math.atan2(moveY - centerY, moveX - centerX);
                double rad = currentRad - lastRad;
                return (float) Math.toDegrees(rad);
            }

            private float getDistance(float x, float y) {
                float tempX = Math.abs(x - centerX);
                float tempY = Math.abs(y - centerY);
                return (float) Math.sqrt(tempX * tempX + tempY * tempY);
            }
        });
    }
}

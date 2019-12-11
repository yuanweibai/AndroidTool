package rango.tool.androidtool.falling.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import rango.tool.androidtool.R;
import rango.tool.androidtool.falling.BaseFallingBean;
import rango.tool.androidtool.falling.BaseFallingSurfaceView;

public class FallingImageSurfaceView extends BaseFallingSurfaceView {

    private static final int FALLING_ITEM_COUNT = 6;
    protected Matrix contentMatrix;
    protected Paint contentPaint;
    private Bitmap bitmap;

    public FallingImageSurfaceView(Context context) {
        this(context, null);
    }

    public FallingImageSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingImageSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    @Override
    protected void createFallingItems(int minFallingSpeed) {
        if (bitmap == null) {
            return;
        }

        for (int i = 0; i < FALLING_ITEM_COUNT; i++) {
            fallingBeanList.add(new FallingImageBean(bitmap, getWidth(), minFallingSpeed));
        }
    }

    @Override
    protected void onSurfaceViewDraw(Canvas canvas) {
        for (BaseFallingBean bean : fallingBeanList) {

            if (!(bean instanceof FallingImageBean)) {
                return;
            }

            FallingImageBean fallingItem = (FallingImageBean) bean;

            if (fallingItem.posX < (-fallingItem.bitmap.getWidth()) || fallingItem.posX > (getWidth() + fallingItem.bitmap.getWidth())) {
                continue;
            }

            contentMatrix.reset();
            contentMatrix.postTranslate(fallingItem.posX, fallingItem.posY);
//                contentPaint.setAlpha((int) (fallingItem.alpha));

            canvas.drawBitmap(fallingItem.bitmap, contentMatrix, contentPaint);
        }
    }

    private void init() {
        contentPaint = new Paint();
        contentMatrix = new Matrix();

        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
    }
}

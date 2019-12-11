package rango.tool.androidtool.falling.image;

import android.graphics.Bitmap;

import java.util.Random;

import rango.tool.androidtool.falling.BaseFallingBean;
import rango.tool.common.utils.ScreenUtils;

public class FallingImageBean implements BaseFallingBean {

    float posX;
    float posY;
    float increaseDistance;
    float alpha;
    Bitmap bitmap;
    private Random random;

    FallingImageBean(Bitmap bitmap, int width, float minFallingSpeed) {
        this.bitmap = bitmap;
        random = new Random();

        posX = getX(width);
        this.posY = -bitmap.getHeight();
        this.alpha = 1f;

        this.increaseDistance = ScreenUtils.dp2px((random.nextFloat() + 1) * minFallingSpeed);
    }


    @Override
    public void updateData(int width, int height, float intervalCoefficient) {
        posY += intervalCoefficient * increaseDistance;
        this.alpha = 1f;

        if (posY > height) {
            if (width > 0) {
                posX = getX(width);
            } else {
                posX = 0;
            }
            posY = -bitmap.getHeight();
        }
    }

    private float getX(int width) {
        float x;
        int bitmapWidth = bitmap.getWidth();
        int maxWidth = width + bitmap.getWidth();
        float tempX = random.nextFloat() * maxWidth;
        if (tempX < bitmapWidth) {
            x = 0 - tempX;
        } else {
            x = tempX - bitmapWidth;
        }
        return x;
    }
}

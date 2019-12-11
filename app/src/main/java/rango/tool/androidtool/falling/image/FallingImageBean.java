package rango.tool.androidtool.falling.image;

import android.graphics.Bitmap;

import java.util.Random;

import rango.tool.androidtool.falling.BaseFallingBean;
import rango.tool.common.utils.ScreenUtils;

public class FallingImageBean implements BaseFallingBean {

    float posX;
    float posY;
    float increaseDistance;
    int alpha;
    Bitmap bitmap;
    private Random random;
    private boolean isEnd;
    boolean isAlreadyEnd;

    FallingImageBean(Bitmap bitmap, int width, float fallingSpeed) {
        this.bitmap = bitmap;
        random = new Random();
        isEnd = false;
        isAlreadyEnd = false;

        posX = getX(width);
        this.posY = -bitmap.getHeight();
        this.alpha = 255;

        this.increaseDistance = ScreenUtils.dp2px((random.nextFloat() + 1) * fallingSpeed);
    }


    @Override
    public void updateData(int width, int height) {
        posY += increaseDistance;
        if (posY > height / 2f) {
            alpha = (int) ((1 - (posY / (height / 2) - 1)) * 255);
        }

        if (posY > height) {
            posX = getX(width);
            alpha = 255;
            if (!isEnd) {
                posY = -bitmap.getHeight();
            } else {
                isAlreadyEnd = true;
            }
        }
    }

    @Override
    public void notifyEnd() {
        isEnd = true;
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

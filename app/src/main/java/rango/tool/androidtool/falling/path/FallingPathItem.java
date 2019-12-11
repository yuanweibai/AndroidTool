package rango.tool.androidtool.falling.path;

import android.graphics.Path;

import java.util.Random;

import rango.tool.androidtool.falling.BaseFallingBean;
import rango.tool.common.utils.ScreenUtils;

public class FallingPathItem implements BaseFallingBean {

    private static final float DISTRIBUTE_RATIO_IN_WIDTH = 1.4f;
    private static final float DIRECTION_ANGLE_RANGE = 0.1f;

    float posX;
    float posY;

    float increaseDistance;
    float scaleRatio;
    float rotateAngle;
    float directionAngle;

    float increaseAngle;

    int alpha;
    int color;
    Path path;
    private boolean isEnd;
    boolean isAlreadyEnd;

    /**
     * @param posX
     * @param posY
     * @param path
     * @param alpha
     * @param color
     * @param fallingSpeed dp
     */
    FallingPathItem(float posX, float posY, Path path, float alpha, int color, float fallingSpeed) {
        this.posX = posX;
        this.posY = posY;
        this.alpha = (int) (255 * alpha);
        this.color = color;
        this.path = path;
        isEnd = false;
        isAlreadyEnd = false;

        Random random = new Random();
        this.rotateAngle = random.nextInt(360);
        this.increaseAngle = random.nextFloat() * 0.5f + 0.5f;
        this.scaleRatio = random.nextFloat() * 0.7f + 0.3f;

        this.increaseDistance = ScreenUtils.dp2px((random.nextFloat() + 1) * fallingSpeed);
        this.directionAngle = (float) (random.nextFloat() * DIRECTION_ANGLE_RANGE + (Math.PI - DIRECTION_ANGLE_RANGE) / 2f);
    }

    @Override
    public void updateData(int width, int height) {
        posX += increaseDistance * Math.cos(directionAngle);
        posY += increaseDistance * Math.sin(directionAngle);

        Random random = new Random();

        directionAngle += (random.nextFloat() - 0.5f) * 0.02f;
        rotateAngle += increaseAngle + random.nextFloat() * 0.1f;

        if (posY > height) {
            if (width > 0) {
                posX = random.nextInt(width) * DISTRIBUTE_RATIO_IN_WIDTH - (DISTRIBUTE_RATIO_IN_WIDTH / 2f - 0.5f) * width;
            } else {
                posX = 0;
            }

            if (!isEnd) {
                posY = 0;
            } else {
                isAlreadyEnd = true;
            }
        }
    }

    @Override
    public void notifyEnd() {
        isEnd = true;
    }
}

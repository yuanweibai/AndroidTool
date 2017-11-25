package rango.tool.androidtool.view.util;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by baiyuanwei on 17/11/17.
 * <p>
 * 缺点：
 * 1、无法抗锯齿，圆角过大，导致圆角处出现锯齿，小的话，没有关系；
 * 2、canvas.clipPath(path,Region.op.REPLACE) is dangerous：
 * 当圆角的图片出现在可滚动的list中，会把此图片上层的view替换掉，目前只有在Vivo Y67 API 23的机子上出现；
 * 解决方案：用canvas.clipPath(path)就可以了；
 */

public class RoundUiHelper {

    private int mRadius;

    // Pre-allocated objects
    private Path mPath;
    private RectF mRectF;

    public void init(int radius) {
        mRadius = radius;
        mPath = new Path();
        mRectF = new RectF();
    }

    public void clip(Canvas canvas, int width, int height) {
        mRectF.set(0, 0, width, height);
        mPath.reset();
        mPath.addRoundRect(mRectF, mRadius, mRadius, Path.Direction.CW);
        canvas.clipPath(mPath); //Region.Op.REPLACE is dangerous.
    }
}

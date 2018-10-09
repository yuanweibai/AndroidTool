package rango.tool.androidtool.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;

public class BitmapUtils {

    /**
     * 把图片处理成圆形，并且加一个圆形白框
     *
     * @param bitmap
     * @param size   单位是px
     * @return
     */
    public static Bitmap addWhiteCircleBorderToBitmap(Bitmap bitmap, int size) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int cx = width / 2;
        int cy = height / 2;
        int diameter = Math.min(width, height);
        int maxRadius = diameter / 2;

        //create new bitmap
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        //draw circle white border
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, maxRadius - size / 2, paint);

        //draw circle bitmap
        BitmapShader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        float scale = diameter / (float) Math.max(width, height);
        matrix.setScale(scale, scale);
        float dx = (width - width * scale) / 2f;
        float dy = (height - height * scale) / 2f;
        matrix.postTranslate(dx, dy);
        mBitmapShader.setLocalMatrix(matrix);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        paint.setShader(mBitmapShader);
        canvas.drawCircle(width / 2f, height / 2f, maxRadius - size, paint);

        return result;
    }
}

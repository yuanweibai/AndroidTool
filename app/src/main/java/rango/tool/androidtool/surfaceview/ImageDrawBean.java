package rango.tool.androidtool.surfaceview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.androidtool.surfaceview.lib.DrawableBeanAdapter;
import rango.tool.common.utils.ScreenUtils;

public class ImageDrawBean extends DrawableBeanAdapter {

    private Bitmap bitmap;
    private Paint bitmapPaint;
    private RectF rectF;

    public ImageDrawBean() {
        bitmapPaint = new Paint();
        bitmapPaint.setFilterBitmap(true);
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setDither(true);

        Context context = ToolApplication.getContext();
        Resources resources = context.getResources();
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.pig_sty_icon);

        rectF = new RectF();
        rectF.left = ScreenUtils.dp2px(160);
        rectF.top = ScreenUtils.dp2px(300);
        rectF.right = rectF.left + bitmap.getWidth();
        rectF.bottom = rectF.top + bitmap.getHeight();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rectF, bitmapPaint);
    }
}
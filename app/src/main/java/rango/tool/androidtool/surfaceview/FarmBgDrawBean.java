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

public class FarmBgDrawBean extends DrawableBeanAdapter {

    private Bitmap bgBitmap;
    private Paint bitmapPaint;
    private RectF rectF;

    public FarmBgDrawBean() {
        bitmapPaint = new Paint();
        bitmapPaint.setFilterBitmap(true);
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setDither(true);

        Context context = ToolApplication.getContext();
        Resources resources = context.getResources();
        bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.pig_farm_bg);

        rectF = new RectF();
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = ScreenUtils.getScreenWidthPx();
        rectF.bottom = ScreenUtils.getScreenHeightPx();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bgBitmap, null, rectF, bitmapPaint);
    }
}
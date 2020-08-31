package rango.tool.androidtool.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.ToolApplication;
import rango.tool.common.utils.ScreenUtils;

public class DrawImageView extends View {
    private Bitmap bitmap;
    private Paint bitmapPaint;
    private RectF rectF;

    public void init() {
        bitmapPaint = new Paint();
//        bitmapPaint.setFilterBitmap(true);
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setColor(Color.BLACK);
        bitmapPaint.setStyle(Paint.Style.STROKE);
        bitmapPaint.setStrokeWidth(40);


        Context context = ToolApplication.getContext();
        Resources resources = context.getResources();
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.pig_sty_icon);

        rectF = new RectF();
        rectF.left = ScreenUtils.dp2px(60);
        rectF.top = ScreenUtils.dp2px(600);
        rectF.right = rectF.left + bitmap.getWidth();
        rectF.bottom = rectF.top + bitmap.getHeight();
    }

    public DrawImageView(Context context) {
        this(context, null);
    }

    public DrawImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawBitmap(bitmap, null, rectF, bitmapPaint);
        Path path = new Path();
        path.moveTo(300,600);
        path.lineTo(300,1000);

        RectF oval = new RectF(500, 200, 800, 800);
        path.addArc(oval, 0, 180);

        path.arcTo(oval, 0, 90);
        RectF oval2 = new RectF(500, 800, 900, 1200);
        path.arcTo(oval2, 90, 180);

        canvas.drawPath(path,bitmapPaint);
    }
}

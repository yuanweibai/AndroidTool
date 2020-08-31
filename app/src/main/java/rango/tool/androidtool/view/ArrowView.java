package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ArrowView extends View {

    private int width;
    private int height;
    private Paint paint;
    private float paintWidth;

    public ArrowView(Context context) {
        this(context, null);
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paintWidth = 10;
        paint.setStrokeWidth(paintWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawLine(paintWidth / 2f, height / 2f + paintWidth / 2f, 2 * width / 3f, paintWidth / 2f, paint);
        canvas.drawLine(paintWidth / 2f, height / 2f, width, height / 2f, paint);
        canvas.drawLine(paintWidth / 2f, height / 2f - paintWidth / 2f, 2 * width / 3f, height - paintWidth / 2f, paint);
        canvas.restore();
    }
}

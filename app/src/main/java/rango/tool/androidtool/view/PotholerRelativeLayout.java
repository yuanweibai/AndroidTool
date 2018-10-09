package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ScreenUtils;

public class PotholerRelativeLayout extends RelativeLayout {

    private Paint potholerPaint;

    public PotholerRelativeLayout(Context context) {
        this(context, null);
    }

    public PotholerRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PotholerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        potholerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        potholerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.potholer_bg_color));

        int cx = ScreenUtils.getScreenWidthPx() / 2;
        int cy = ScreenUtils.getScreenHeightPx() - 400;
        canvas.drawCircle(cx, cy, 200, potholerPaint);

        super.dispatchDraw(canvas);
    }
}

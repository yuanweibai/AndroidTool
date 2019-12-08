package rango.tool.androidtool.falling;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class FallingImageSurfaceView extends FallingSurfaceView {

    public FallingImageSurfaceView(Context context) {
        super(context);
    }

    public FallingImageSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FallingImageSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void createFallingItems(int minFallingSpeed) {

    }

    @Override
    protected void onSurfaceViewDraw(Canvas canvas, FallingPathItem fallingItem) {

    }
}

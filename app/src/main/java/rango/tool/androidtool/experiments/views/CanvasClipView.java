package rango.tool.androidtool.experiments.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import rango.tool.androidtool.R;

/**
 * Created by baiyuanwei on 17/11/20.
 */

public class CanvasClipView extends AppCompatImageView {

    private static final String TAG = CanvasClipView.class.getSimpleName();

    private Paint paint;
    private Region.Op op;
    private Path path;
    private RectF rectF;
    private RectF rectFB;

    public CanvasClipView(Context context) {
        this(context, null);
    }

    public CanvasClipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CanvasClipView, defStyleAttr, 0);
        int type = ta.getInteger(R.styleable.CanvasClipView_op_type, -1);
        ta.recycle();
        op = initOp(type);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        rectF = new RectF();
        rectFB = new RectF();
    }

    private Region.Op initOp(int opType) {
        switch (opType) {
            case 0:
                return Region.Op.DIFFERENCE;
            case 1:
                return Region.Op.INTERSECT;
            case 2:
                return Region.Op.UNION;
            case 3:
                return Region.Op.XOR;
            case 4:
                return Region.Op.REVERSE_DIFFERENCE;
            case 5:
                return Region.Op.REPLACE;
            default:
                return null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        paint.setColor(Color.RED);
        rectF.set(0, 0, width / 2, height - 20);
        rectFB.set(width / 4, height / 4, width * 3 / 4, height * 3 / 4);
//        canvas.drawRect(rectF, paint);


        if (op != null) {
            Log.e(TAG, "op = " + op);
            path.reset();
            path.addRect(rectFB, Path.Direction.CW);
            canvas.clipPath(path, op);
        }

        super.onDraw(canvas);

    }
}

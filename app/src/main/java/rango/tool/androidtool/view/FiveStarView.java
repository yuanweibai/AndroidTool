package rango.tool.androidtool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by baiyuanwei on 17/11/21.
 */

public class FiveStarView extends View {

    private Paint paint;
    private Path path;
    private int width;
    private int height;

    public FiveStarView(Context context) {
        this(context, null);
    }

    public FiveStarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FiveStarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();

        canvas.save();
        path.reset();
        path.moveTo(width * 0.5f, 0);
        path.lineTo(width * 0.683f, height * 0.295f);
        path.lineTo(width, 0.385f * height);
        path.lineTo(0.793f * width, 0.667f * height);
        path.lineTo(0.817f * width, height);
        path.lineTo(0.5f * width, 0.872f * height);
        path.lineTo(0.183f * width, height);
        path.lineTo(0.207f * width, 0.667f * height);
        path.lineTo(0, 0.385f * height);
        path.lineTo(0.317f * width, 0.295f * height);
        path.lineTo(0.5f * width, 0);
        path.close();
        canvas.clipPath(path);
        canvas.drawRect(new RectF(0, 0, width, height), paint);
        canvas.restore();
    }
}

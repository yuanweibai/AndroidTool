package rango.tool.androidtool.view;

import android.content.Context;
import android.util.AttributeSet;

public class RoundImageView extends ShapeImageView {

    public RoundImageView(Context context) {
        this(context, null, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mShape = Shape.RECTANGLE;
    }
}

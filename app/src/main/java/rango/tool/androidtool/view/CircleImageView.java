package rango.tool.androidtool.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by baiyuanwei on 17/11/22.
 */

public class CircleImageView extends ShapeImageView {

    public CircleImageView(Context context) {
        this(context, null, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mShape = Shape.CIRCLE;
    }

}

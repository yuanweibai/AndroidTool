package rango.tool.androidtool.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by baiyuanwei on 17/11/22.
 */

public class OvalImageView extends ShapeImageView {

    public OvalImageView(Context context) {
        this(context, null, 0);
    }

    public OvalImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mShape = Shape.OVAL;
    }
}

package rango.tool.androidtool.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import rango.tool.androidtool.R;
import rango.tool.androidtool.view.util.RoundUiHelper;

/**
 * Created by baiyuanwei on 17/11/17.
 * <p>
 * 缺点：不能抗锯齿，圆角过大，会导致圆角出现锯齿，小的话，没有关系
 */

public class RoundByClipImageView extends AppCompatImageView {
    private RoundUiHelper roundUiHelper;

    public RoundByClipImageView(Context context) {
        this(context, null);
    }

    public RoundByClipImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundByClipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundByClipImageView, defStyleAttr, 0);
        int radius = ta.getDimensionPixelSize(R.styleable.RoundByClipImageView_round_radius, 0);
        ta.recycle();
        roundUiHelper = new RoundUiHelper();
        roundUiHelper.init(radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        roundUiHelper.clip(canvas, getWidth(), getHeight());
        super.onDraw(canvas);
    }
}

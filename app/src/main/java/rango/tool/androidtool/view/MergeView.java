package rango.tool.androidtool.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import rango.tool.androidtool.R;

public class MergeView extends LinearLayout {

    public MergeView(Context context) {
        this(context, null);
    }

    public MergeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MergeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_tag, this);
    }
}

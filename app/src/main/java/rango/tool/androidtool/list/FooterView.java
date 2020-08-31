package rango.tool.androidtool.list;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.list.adapter.BaseItemView;

public class FooterView extends BaseItemView {

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(Color.BLUE);
        LayoutInflater.from(context).inflate(R.layout.footer_layout, this);

        initView();
    }

    private void initView(){

    }

    @Override
    protected void refreshUI() {

    }
}

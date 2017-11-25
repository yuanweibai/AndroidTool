package rango.tool.androidtool.list.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.recyclerview.BaseItemView;

/**
 * Created by baiyuanwei on 17/11/16.
 */

public class ListBannerView extends BaseItemView {

    private static final String TAG = ListBannerView.class.getSimpleName();

    public ListBannerView(Context context) {
        this(context, null);
    }

    public ListBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.list_banner_item_view, this);
        initView();
    }

    private void initView() {

    }

    @Override
    protected void refreshUI() {

    }


    @Override
    protected void onDetachedFromWindow() {
        Log.e(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        Log.e(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }
}

package rango.tool.androidtool.list.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.recyclerview.BaseItemView;

/**
 * Created by baiyuanwei on 17/11/16.
 */

public class ListEmptyItemView extends BaseItemView {

    private View view;
    private int height;

    public ListEmptyItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.empty_item_view, this);
        initView();
    }

    private void initView() {
        view = findViewById(R.id.empty_view);
    }

    @Override
    protected void refreshUI() {
        if (mData == null || mData.getData() == null) {
            return;
        }
        height = (int) mData.getData();
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }
}

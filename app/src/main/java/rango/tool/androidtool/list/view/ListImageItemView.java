package rango.tool.androidtool.list.view;

import android.content.Context;
import android.view.LayoutInflater;

import rango.tool.androidtool.R;
import rango.tool.androidtool.base.list.adapter.BaseItemView;

public class ListImageItemView extends BaseItemView {

    public ListImageItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.list_image_item_layout, this);
    }

    @Override
    protected void refreshUI() {
        if (mData == null || mData.getData() == null) {
            return;
        }

    }
}

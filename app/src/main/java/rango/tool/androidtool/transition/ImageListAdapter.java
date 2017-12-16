package rango.tool.androidtool.transition;

import android.view.ViewGroup;

import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemView;
import rango.tool.androidtool.base.list.adapter.BaseRecyclerViewAdapter;

public class ImageListAdapter extends BaseRecyclerViewAdapter<BaseItemData, BaseItemView> {

    @Override
    protected BaseItemView onCreateItemView(ViewGroup parent, int viewType) {
        return new ImageItemView(parent.getContext());
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }
}

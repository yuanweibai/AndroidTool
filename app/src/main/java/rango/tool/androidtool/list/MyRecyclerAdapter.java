package rango.tool.androidtool.list;

import android.view.ViewGroup;

import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemView;
import rango.tool.androidtool.base.list.adapter.BaseRecyclerViewAdapter;


public class MyRecyclerAdapter extends BaseRecyclerViewAdapter<BaseItemData, BaseItemView> {

    private ListGenerator listGenerator;

    public MyRecyclerAdapter() {
        listGenerator = new ListGenerator();
    }

    @Override
    protected BaseItemView onCreateItemView(ViewGroup parent, int viewType) {
        return listGenerator.createView(parent.getContext(), viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }
}


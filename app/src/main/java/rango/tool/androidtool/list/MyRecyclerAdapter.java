package rango.tool.androidtool.list;

import android.view.ViewGroup;

import rango.tool.androidtool.base.recyclerview.BaseItemData;
import rango.tool.androidtool.base.recyclerview.BaseItemView;
import rango.tool.androidtool.base.recyclerview.BaseRecyclerViewAdapter;

/**
 * Created by baiyuanwei on 17/11/16.
 */

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


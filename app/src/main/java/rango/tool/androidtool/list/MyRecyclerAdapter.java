package rango.tool.androidtool.list;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;

import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;
import rango.tool.androidtool.base.list.adapter.BaseItemView;
import rango.tool.androidtool.base.list.adapter.BaseRecyclerViewAdapter;
import rango.tool.androidtool.base.list.adapter.ViewWrapper;
import rango.tool.androidtool.list.view.ListNestItemView;


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

    @Override
    public void onViewRecycled(@NonNull ViewWrapper<BaseItemData, BaseItemView> holder) {

        if (holder.getItemViewType() == BaseItemType.TYPE_LIST_NEST) {
            ((ListNestItemView) holder.getView()).saveInstanceState();
        }
    }
}


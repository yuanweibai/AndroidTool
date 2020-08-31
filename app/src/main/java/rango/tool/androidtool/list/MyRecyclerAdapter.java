package rango.tool.androidtool.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return BaseItemType.TYPE_LIST_FOOTER == getItemViewType(position) ? ((GridLayoutManager) layoutManager).getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewWrapper<BaseItemData, BaseItemView> holder) {
        if (holder.getItemViewType() == BaseItemType.TYPE_LIST_FOOTER) {
            ViewGroup.LayoutParams params = holder.getView().getLayoutParams();
            if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
            }
        }
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


package rango.tool.androidtool.base.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, V extends View & ViewWrapper.Binder<T>> extends RecyclerView.Adapter<ViewWrapper<T, V>> {

    protected List<T> mItems = new ArrayList<>();

    @Override
    public final ViewWrapper<T, V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(onCreateItemView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(ViewWrapper<T, V> holder, int position) {
        V view = holder.getView();
        T data = getItem(position);
        view.bind(data);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItems() {
        return mItems;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    public void update(List<T> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void update(int position, T item) {
        mItems.set(position, item);
        notifyItemChanged(position);
    }

    public void append(T item) {
        append(mItems.size(), item);
    }

    public void append(int position, T item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void append(List<T> items) {
        append(mItems.size(), items);
    }

    public void append(int position, List<T> items) {
        mItems.addAll(items);
        notifyItemRangeChanged(position, items.size());
    }

    public void remove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }
}

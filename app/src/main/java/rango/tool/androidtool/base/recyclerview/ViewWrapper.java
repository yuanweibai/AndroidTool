package rango.tool.androidtool.base.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ViewWrapper<T, V extends View & ViewWrapper.Binder<T>> extends RecyclerView.ViewHolder {

    private V mView;

    public ViewWrapper(V itemView) {
        super(itemView);
        this.mView = itemView;
    }

    public V getView() {
        return mView;
    }

    public interface Binder<T> {
        void bind(T data);
    }
}

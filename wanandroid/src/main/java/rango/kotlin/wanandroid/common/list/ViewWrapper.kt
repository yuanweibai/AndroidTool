package rango.kotlin.wanandroid.common.list

import androidx.recyclerview.widget.RecyclerView

class ViewWrapper(val view: BaseItemView) : RecyclerView.ViewHolder(view) {

    interface Binder {
        fun bind(data: BaseItemData<*>?)
    }

}
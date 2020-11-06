package rango.kotlin.wanandroid.common.list

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*

abstract class BaseRecyclerViewAdapter : RecyclerView.Adapter<ViewWrapper>() {

    private var mItems: MutableList<BaseItemData<*>> = ArrayList()

    protected abstract fun onCreateItemView(parent: ViewGroup, viewType: Int): BaseItemView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewWrapper {
        val itemView = onCreateItemView(parent, viewType)
        itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return ViewWrapper(itemView)
    }

    override fun onBindViewHolder(holder: ViewWrapper, position: Int) {
        val view = holder.view
        val data = getItem(position)
        view.bind(data)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isFullSpan(getItemViewType(position))) layoutManager.spanCount else 1
                }
            }
        }
    }

    private fun isFullSpan(type: Int): Boolean {
        return type == BaseItemType.TYPE_COMMON_LOAD_MORE_ITEM
    }

    override fun onViewAttachedToWindow(holder: ViewWrapper) {
        if (isFullSpan(holder.itemViewType)) {
            val params = holder.view.layoutParams
            if (params is StaggeredGridLayoutManager.LayoutParams) {
                params.isFullSpan = true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    val items: List<BaseItemData<*>>
        get() = mItems

    fun update(items: MutableList<BaseItemData<*>>) {
        mItems = items
        notifyDataSetChanged()
    }

    fun update(position: Int, item: BaseItemData<*>) {
        mItems[position] = item
        notifyItemChanged(position)
    }

    fun append(item: BaseItemData<*>) {
        append(mItems.size, item)
    }

    fun append(position: Int, item: BaseItemData<*>) {
        mItems.add(position, item)
        notifyItemInserted(position)
    }

    fun append(items: List<BaseItemData<*>>) {
        append(mItems.size, items)
    }

    fun append(position: Int, items: List<BaseItemData<*>>) {
        val isNotifyAll = position == mItems.size
        mItems.addAll(position, items)
        if (isNotifyAll) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(position, items.size)
        }
    }

    fun remove(position: Int) {
        mItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mItems.size)
    }

    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): BaseItemData<*> {
        return mItems[position]
    }
}
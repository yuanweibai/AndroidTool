package rango.kotlin.wanandroid.home

import android.view.ViewGroup
import rango.kotlin.wanandroid.common.list.BaseItemType
import rango.kotlin.wanandroid.common.list.BaseItemView
import rango.kotlin.wanandroid.common.list.BaseRecyclerViewAdapter

class HomeRecyclerAdapter : BaseRecyclerViewAdapter() {

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): BaseItemView {
        val context = parent.context
        return when (viewType) {
            BaseItemType.TYPE_COMMON_LOAD_MORE_ITEM -> {
                HomeLoadMoreItemView(context)
            }
            else -> {
                HomeArticleItemView(context)
            }
        }

    }
}
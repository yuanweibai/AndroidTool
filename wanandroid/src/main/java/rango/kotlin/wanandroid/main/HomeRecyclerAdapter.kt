package rango.kotlin.wanandroid.main

import android.util.AttributeSet
import android.view.ViewGroup
import rango.kotlin.wanandroid.common.list.BaseItemView
import rango.kotlin.wanandroid.common.list.BaseRecyclerViewAdapter

class HomeRecyclerAdapter : BaseRecyclerViewAdapter() {

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): BaseItemView {
        return HomeArticleItemView(parent.context)
    }
}
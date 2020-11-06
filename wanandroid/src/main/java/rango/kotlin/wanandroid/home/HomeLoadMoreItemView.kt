package rango.kotlin.wanandroid.home

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import rango.kotlin.wanandroid.R
import rango.kotlin.wanandroid.common.list.BaseItemView

class HomeLoadMoreItemView constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) : BaseItemView(context, attributeSet, defStyle) {

    private val msgTextView: TextView = TextView(context)
    private var homeLoadMoreData: HomeLoadMoreData? = null

    init {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        msgTextView.text = context.getString(R.string.loading_text)
        msgTextView.gravity = Gravity.CENTER_HORIZONTAL
        msgTextView.setPadding(0, 50, 0, 50)
        msgTextView.setTextColor(Color.BLACK)
        msgTextView.textSize = 16f
        addView(msgTextView, params)

        msgTextView.setOnClickListener {
            if (homeLoadMoreData?.status != HomeLoadMoreData.STATUS_LOADING_FAILURE) {
                return@setOnClickListener
            }

            homeLoadMoreData?.status = HomeLoadMoreData.STATUS_LOADING
            refreshStatus(homeLoadMoreData?.status ?: 0)

            homeLoadMoreData?.action?.run()
        }
    }

    override fun refreshUI() {

        if (mData == null || mData?.data == null) {
            return
        }
        homeLoadMoreData = mData?.data as HomeLoadMoreData

        refreshStatus(homeLoadMoreData?.status ?: 0)
    }

    private fun refreshStatus(status: Int) {
        when (status) {
            HomeLoadMoreData.STATUS_LOADING -> {
                msgTextView.isClickable = false
                msgTextView.text = context.getString(R.string.loading_text)

            }
            HomeLoadMoreData.STATUS_LOADING_FAILURE -> {
                msgTextView.isClickable = true
                msgTextView.text = context.getString(R.string.loading_failure_text)
            }
        }
    }
}
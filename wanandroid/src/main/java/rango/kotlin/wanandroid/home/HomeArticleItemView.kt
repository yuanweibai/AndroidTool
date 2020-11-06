package rango.kotlin.wanandroid.home

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.wa_home_item_article_layout.view.*
import rango.kotlin.wanandroid.R
import rango.kotlin.wanandroid.common.list.BaseItemView

class HomeArticleItemView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) : BaseItemView(context, attributeSet, defStyle) {


    private var articleTitleData: ArticleTitleData? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.wa_home_item_article_layout, this)
//        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//        layoutParams.leftToLeft = LayoutParams.PARENT_ID
//        layoutParams.rightToRight = LayoutParams.PARENT_ID
//        layoutParams.topToTop = LayoutParams.PARENT_ID
//        addView(view, layoutParams)
        setBackgroundColor(Color.parseColor("#FF9800"))
    }

    override fun refreshUI() {
        if (mData == null || mData?.data == null) {
            return
        }

        articleTitleData = mData?.data as ArticleTitleData

        nameText.text = articleTitleData?.name
        flagText.text = articleTitleData?.flag
        titleText.text = articleTitleData?.title
    }
}
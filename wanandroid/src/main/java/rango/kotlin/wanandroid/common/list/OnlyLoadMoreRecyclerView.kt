package rango.kotlin.wanandroid.common.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class OnlyLoadMoreRecyclerView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int = 0) : RecyclerView(context, attributeSet, defStyle) {

    companion object{
        const val DEFAULT_POSITION_BACK = 3
    }

    private var isLoadingMore = false
    private var positionBackToLoadMore = DEFAULT_POSITION_BACK

    private var loadMoreListener: OnLoadMoreListener? = null

    private var smoothToPosition = 0
    private var shouldScrollToPosition = false

    init {
        init()
    }

    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener?) {
        this.loadMoreListener = loadMoreListener
    }

    fun onLoadMoreFinished() {
        isLoadingMore = false
    }

    fun setPositionBackToLoadMore(positionBack: Int) {
        check(positionBack >= 0) { "positionBack could not smaller than 0!!!" }
        positionBackToLoadMore = positionBack
    }

    private fun init() {
        setHasFixedSize(true)
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                smoothScroll(newState)
                loadMore()
            }

            private fun smoothScroll(newState: Int) {
                if (shouldScrollToPosition && newState == SCROLL_STATE_IDLE) {
                    shouldScrollToPosition = false
                    smoothToPosition(smoothToPosition)
                }
            }

            private fun loadMore() {
                if (isLoadingMore) {
                    return
                }
                if (isCouldLoadMore()) {
                    isLoadingMore = true
                    loadMoreListener?.onLoadMore()
                }
            }
        })
    }

    private fun findLastVisibleItemPosition(): Int {
        return when (layoutManager) {
            is LinearLayoutManager -> {
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                val positionArray = (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                positionArray[0]
            }
            else -> {
                throw IllegalStateException("layoutManager is unknown!!!")
            }
        }
    }

    private fun isCouldLoadMore(): Boolean {
        val lastVisiblePosition = findLastVisibleItemPosition()
        return lastVisiblePosition >= getLastPosition() - positionBackToLoadMore
    }

    private fun getLastPosition(): Int {
        val layoutManager = layoutManager ?: return 0
        return layoutManager.itemCount - 1
    }

    fun smoothToPosition(position: Int) {
        val firstVisibleItemPosition = getChildLayoutPosition(getChildAt(0))
        val lastVisibleItemPosition = getChildLayoutPosition(getChildAt(childCount - 1))
        if (position < firstVisibleItemPosition) {
            smoothScrollToPosition(position)
        } else if (position <= lastVisibleItemPosition) {
            val movePosition = position - firstVisibleItemPosition
            if (movePosition in 0 until childCount) {
                val top = getChildAt(movePosition).top
                smoothScrollBy(0, top)
            }
        } else {
            smoothScrollToPosition(position)
            smoothToPosition = position
            shouldScrollToPosition = true
        }
    }

}
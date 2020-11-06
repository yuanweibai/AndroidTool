package rango.kotlin.wanandroid.home

import android.graphics.Color

data class HomeLoadMoreData @JvmOverloads constructor(var status: Int, val action: Runnable? = null) {
    companion object {
        const val STATUS_LOADING = 1
        const val STATUS_LOADING_FAILURE = 2
    }
}
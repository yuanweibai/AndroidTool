package rango.kotlin.wanandroid.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rango.kotlin.wanandroid.common.http.api.bean.ArticleListBean
import rango.kotlin.wanandroid.common.http.lib.FailureData
import rango.kotlin.wanandroid.common.http.lib.httpRequest
import rango.kotlin.wanandroid.common.http.lib.httpService

class HomeViewModel : ViewModel() {

    val homeLiveData: MutableLiveData<ArticleListBean> by lazy {
        MutableLiveData<ArticleListBean>()
    }

    val requestErrorLiveData: MutableLiveData<FailureData> by lazy {
        MutableLiveData<FailureData>()
    }

    fun refreshHomeData() {
        requestHomeData(0)
    }

    fun loadMoreHomeData() {
        val page = homeLiveData.value?.curPage ?: 0
        requestHomeData(page)
    }

    private fun requestHomeData(page: Int) {
        httpRequest({ httpService.getArticleList(page) }, homeLiveData, requestErrorLiveData)
    }


}

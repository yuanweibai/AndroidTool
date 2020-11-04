package rango.kotlin.wanandroid.main

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

    fun requestHomeData(pageNo: Int) {
        httpRequest({ httpService.getArticleList(pageNo) }, homeLiveData, requestErrorLiveData)
    }


}

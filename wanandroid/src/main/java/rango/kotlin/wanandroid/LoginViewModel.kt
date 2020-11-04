package rango.kotlin.wanandroid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rango.kotlin.wanandroid.common.http.lib.FailureData
import rango.kotlin.wanandroid.common.http.api.bean.LoginBean
import rango.kotlin.wanandroid.common.http.lib.httpRequest
import rango.kotlin.wanandroid.common.http.lib.httpService

class LoginViewModel : ViewModel() {

    val loginSuccessLiveData: MutableLiveData<LoginBean> by lazy {
        MutableLiveData<LoginBean>()
    }

    val loginErrorLiveData: MutableLiveData<FailureData> by lazy {
        MutableLiveData<FailureData>()
    }

    val loginStatusLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun login(name: String, password: String) {
        loginStatusLiveData.value = "登录中......"
        httpRequest({
            httpService.login(name, password)
        }, loginSuccessLiveData, loginErrorLiveData)
    }
}
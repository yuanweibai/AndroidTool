package rango.kotlin.wanandroid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rango.kotlin.wanandroid.common.http.api.bean.LoginBean
import rango.kotlin.wanandroid.common.http.api.bean.SearchAddressBean
import rango.kotlin.wanandroid.common.http.lib.*

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

    val searchSuccessLiveData: MutableLiveData<SearchAddressBean> by lazy {
        MutableLiveData<SearchAddressBean>()
    }

    val searchFailureLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun login(name: String, password: String) {
        loginStatusLiveData.value = "登录中......"
        httpRequest({
            httpService.login(name, password)
        }, loginSuccessLiveData, loginErrorLiveData)
    }

    fun requestAddress(address: String) {
        viewModelScope.launch {
            val data = async { search(address) }
            Log.e("rango", "data = ${data.await()?.totalResultsCount}, thread = ${Thread.currentThread().name}")
        }
    }

    suspend fun search(address: String): SearchAddressBean? {
        return withContext(Dispatchers.IO) {
            val data = httpService.searchAddress(address).execute()
            Log.e("rango", "code = ${data.code()}, message = ${data.message()}")
            data.body()
        }


    }
}
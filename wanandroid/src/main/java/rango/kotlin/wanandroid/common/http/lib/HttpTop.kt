package rango.kotlin.wanandroid.common.http.lib

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import rango.kotlin.wanandroid.common.http.api.IHttpRequest

const val CODE_CONNECT_ERROR = -1000
const val CODE_OK = 0

fun <T> ViewModel.httpRequest(
        request: suspend () -> Response<T>,
        successLiveData: MutableLiveData<T>,
        failureLiveData: MutableLiveData<FailureData>
) {

    viewModelScope.launch {
        kotlin.runCatching {
            request()
        }.onSuccess {
            if (it.errorCode == CODE_OK) {
                successLiveData.value = it.data
            } else {
                failureLiveData.value = FailureData(it.errorCode, it.errorMsg)
            }
        }.onFailure {
            failureLiveData.value = FailureData(CODE_CONNECT_ERROR, it.message)
        }
    }
}

fun <T> ViewModel.httpRequestIndependent(
        request: suspend () -> T,
        successLiveData: MutableLiveData<T>,
        failureLiveData: MutableLiveData<String>
) {

    viewModelScope.launch {
        kotlin.runCatching {
            request()
        }.onSuccess {
            successLiveData.value = it

        }.onFailure {
            failureLiveData.value = it.message
        }
    }
}

val httpService: IHttpRequest by lazy {
    HttpFactory.getDefaultRetrofit().create(IHttpRequest::class.java)
}

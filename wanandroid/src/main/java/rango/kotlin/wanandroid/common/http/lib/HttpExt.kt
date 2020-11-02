package rango.kotlin.wanandroid.common.http.lib

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import rango.kotlin.wanandroid.common.http.api.IHttpRequest

const val CODE_CONNECT_ERROR = -1
const val CODE_OK = 0

fun <T> ViewModel.httpRequest(
        request: suspend () -> Response<T>,
        successLiveData: MutableLiveData<T>,
        failureLiveData: MutableLiveData<FailureData>
) {

    viewModelScope.launch {
        Log.e("rango-1", "currentThread = " + Thread.currentThread().name)
        kotlin.runCatching {
            Log.e("rango-2", "currentThread = " + Thread.currentThread().name)
            request()
        }.onSuccess {
            Log.e("rango-3", "currentThread = " + Thread.currentThread().name)
            if (it.errorCode == CODE_OK) {
                successLiveData.value = it.data
            } else {
                failureLiveData.value = FailureData(it.errorCode, it.errorMsg)
            }
        }.onFailure {
            Log.e("rango-4", "currentThread = " + Thread.currentThread().name)
            val failureData = FailureData(CODE_CONNECT_ERROR, it.message)
            failureLiveData.value = failureData
        }
    }
}

val httpService: IHttpRequest by lazy {
    HttpFactory.getDefaultRetrofit().create(IHttpRequest::class.java)
}

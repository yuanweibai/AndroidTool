package rango.kotlin.wanandroid.common.http.lib

data class Response<T>(val errorCode: Int, val errorMsg: String, val data: T) {

    companion object {
        const val CODE_OK = 0
    }

    fun isSuccess() = errorCode == CODE_OK
}
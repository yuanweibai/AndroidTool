package rango.kotlin.designmode.mvp.presenter

interface IResultListener {

    fun onSuccess(msg: String)

    fun onFailure(msg: String?)
}
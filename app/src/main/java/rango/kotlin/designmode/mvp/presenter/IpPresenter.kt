package rango.kotlin.designmode.mvp.presenter

import rango.kotlin.designmode.mvp.model.IpModel
import rango.kotlin.designmode.mvp.view.IIpView

class IpPresenter : IResultListener {

    private lateinit var ipView: IIpView
    private lateinit var ipModel: IpModel

    constructor(ipView: IIpView) {
        this.ipView = ipView
        ipModel = IpModel()
    }

    fun search(ip: String) {
        ipView.result("正在加载")
        ipModel.getIpInfo(ip, this)

    }

    override fun onSuccess(msg: String) {
        ipView.result(msg)
    }

    override fun onFailure(msg: String?) {
        ipView.result(msg)
    }
}
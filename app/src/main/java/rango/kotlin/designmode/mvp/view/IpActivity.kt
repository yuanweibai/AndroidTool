package rango.kotlin.designmode.mvp.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import rango.kotlin.designmode.mvp.presenter.IpPresenter
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class IpActivity : BaseActivity(), IIpView {

    lateinit var ipEdit: EditText
    lateinit var loadingText: TextView
    lateinit var ipPresenter: IpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_layout)

        ipEdit = findViewById(R.id.ip_edit)
        loadingText = findViewById(R.id.loading_text)
        ipPresenter = IpPresenter(this)
        findViewById<View>(R.id.search_btn).setOnClickListener {
            ipPresenter.search(ipEdit.text.toString())
        }
    }

    override fun result(msg: String?) {
        loadingText.text = msg
    }
}

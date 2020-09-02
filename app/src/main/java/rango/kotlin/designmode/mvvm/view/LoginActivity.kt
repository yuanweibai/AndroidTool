package rango.kotlin.designmode.mvvm.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import rango.kotlin.designmode.mvvm.viewmodel.LoginViewModel
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity
import rango.tool.androidtool.databinding.LoginBinding

class LoginActivity : BaseActivity() {

    lateinit var loginBinding: LoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = DataBindingUtil.setContentView(this, R.layout.mvvm_activity_login_layout)
        loginBinding.login = LoginViewModel()

    }
}
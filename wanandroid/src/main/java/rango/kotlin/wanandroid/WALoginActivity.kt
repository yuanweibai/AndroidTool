package rango.kotlin.wanandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.wa_activity_login_layout.*
import kotlinx.android.synthetic.main.wa_activity_login_layout.view.*
import rango.kotlin.wanandroid.home.WAMainActivity
import rango.kotlin.wanandroid.common.http.api.bean.LoginBean
import rango.kotlin.wanandroid.common.http.api.bean.SearchAddressBean
import rango.kotlin.wanandroid.common.http.lib.FailureData

class WALoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wa_activity_login_layout)

        loginViewModel.loginSuccessLiveData.observe(this, Observer<LoginBean> {
            loginStatusText.text = "登录成功了，${it.username}!!!"
            Toast.makeText(this, "登录成功了！！！", Toast.LENGTH_LONG).show()
            gotoMainPage()
        })

        loginViewModel.loginErrorLiveData.observe(this, Observer<FailureData> {
            loginStatusText.text = "登陆失败了，code = ${it.errorCode}, msg = ${it.errorMsg}!!!"
            Toast.makeText(this, "登录失败了，请重新登录!!!", Toast.LENGTH_LONG).show()
        })

        loginViewModel.loginStatusLiveData.observe(this, Observer<String> {
            loginStatusText.text = it
        })

        loginViewModel.searchFailureLiveData.observe(this, Observer<String> {
            Log.e("rango", "search failure: $it")
        })

        loginViewModel.searchSuccessLiveData.observe(this, Observer<SearchAddressBean> {
            Log.e("rango", "search success: ${it.geonames.size}")
        })

        loginBtn.setOnClickListener {
            loginViewModel.login(accountEdit.text.toString(), passwordEdit.text.toString())
        }

        skipBtn.setOnClickListener {
            gotoMainPage()
        }

        searchBtn.setOnClickListener {
            loginViewModel.requestAddress("Beijing")
        }
    }

    private fun gotoMainPage() {
        startActivity(Intent(this, WAMainActivity::class.java))
        finish()
    }
}
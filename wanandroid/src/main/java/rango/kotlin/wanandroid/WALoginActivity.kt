package rango.kotlin.wanandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.wa_activity_login_layout.*
import rango.kotlin.wanandroid.common.http.api.IApiServices

class WALoginActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wa_activity_login_layout)

//        IApiServices.dd

    }
}
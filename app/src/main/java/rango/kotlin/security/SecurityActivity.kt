package rango.kotlin.security

import android.os.Bundle
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class SecurityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_layout)

//        val key = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }
}
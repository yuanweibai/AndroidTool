package rango.kotlin

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_all_test.*
import rango.kotlin.coroutines.CoroutinesActivity
import rango.tool.androidtool.R
import rango.tool.androidtool.base.BaseActivity

class AllTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_test)

        coroutinesBtn.setOnClickListener {
            startActivity(CoroutinesActivity::class.java)
        }
    }

}
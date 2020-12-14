package rango.kotlin.wanandroid.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.wa_activity_main_layout.*
import rango.kotlin.wanandroid.R

class WAMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wa_activity_main_layout)


//        val homeFragment = WAHomeFragment()
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.add(R.id.fragment_container, homeFragment)
//        transaction.disallowAddToBackStack()
//        transaction.commit()

        viewPager.adapter = MainFragmentAdapter(this)
    }
}
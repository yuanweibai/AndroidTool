package rango.kotlin.wanandroid.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import rango.kotlin.wanandroid.R

class WAMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wa_activity_main_layout)



        val homeFragment = WAHomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, homeFragment)
        transaction.commit()
    }
}
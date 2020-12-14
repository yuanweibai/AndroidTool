package rango.kotlin.mytest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.*
import rango.kotlin.mytest.People
import rango.kotlin.mytest.share.BASE_URL
import rango.kotlin.mytest.share.getDeviceId
import rango.tool.androidtool.R
import rango.tool.androidtool.databinding.MyTestBinding

class MyTestActivity : AppCompatActivity() {

    lateinit var people: People
    lateinit var map: ObservableArrayMap<String, Any>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        people = People(ObservableField("You"), ObservableInt(9023), ObservableField(""))
        val binding = DataBindingUtil.setContentView<MyTestBinding>(this, R.layout.activity_login_layout)
        binding.people = people
        binding.clickListener = ClickListener()
        map = ObservableArrayMap()
        map["key"] = "2323"
        binding.map = map
    }

    inner class ClickListener {
        fun clickImage() {
            people.nameObservable.set("fadfasd")
            map["key"] = "fajdkflaj"
        }
    }
}
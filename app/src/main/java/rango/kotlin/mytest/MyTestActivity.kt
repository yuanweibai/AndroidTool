package rango.kotlin.mytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.*
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
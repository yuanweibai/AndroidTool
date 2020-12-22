package rango.kotlin.mytest.share

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_null_layout.*
import rango.tool.androidtool.R

class NullActivity : Activity() {

    //    private var name:String
//    private var name:String = ""
    private var name = ""
//    private var name: String? = null

//    private var name:String
//    init {
//        name = ""
//    }

    // 很危险，在使用的时候，根本不知道此变量是定义为 lateinit，如果为空，必然发生崩溃。
//    private lateinit var name:String


    private var data: HttpData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_null_layout)

//        name = "ss"
        val length = name.length
//        val length = name?.length
//        val length = name!!.length // 如果为空，就发生崩溃

        Log.e("test", "length = $length")




        data?.value
        data?.isRegister
        data?.age
        data?.height
        data?.name
        data?.isMarried

        // 返回最后一行表达式的值，用 it 代替该对象
        data?.let {
            valueText.text = it.value.toString()
            it.isRegister
            it.age
            it.height
            it.name
            it.isMarried
        }

        val isMarried:Boolean? = data?.let {
            it.value
            it.isRegister
            it.age
            it.height
            it.name
            it.isMarried
        }
        Log.e("test", "isMarried = $isMarried")

        //返回最后一行表达式的值，用 this 代替该对象
        data?.run {
            value
            isRegister
            age
            height
            name
            isMarried
        }

        val name:String? = data?.run {
            value
            isRegister
            age
            height
            name
        }

        // also 与 let 类似，但是 also 返回的是调用对象本身，而不是最后一行，用 it 代替该对象
//        val httpData = data?.also {
//            it.getAge()
//            it.getHeight()
//            it.getName()
//            it.isMarried()
//        }
//        Log.e("test", "age = ${httpData?.getAge()}")

        // apply 与 run 类似，区别在于 apply 返回的是调用对象本身，而不是最后一行
        val httpData:HttpData? = data?.apply {
            name
            age
            isMarried
            isRegister
        }
        Log.e("test", "name = ${httpData?.name}")

        // 不支持判空，用 this 代替该对象
//        val age = with(data) {
//           getAge()
//        }

//        val age = with(data!!) {
//            getAge()
//        }
//        Log.e("test", "age = $age")


//         val devicesId = getDeviceId()

//        val list = listOf(4, 5, 6, 7, 9, 1, 0, 90)
//        list.print()
    }
}
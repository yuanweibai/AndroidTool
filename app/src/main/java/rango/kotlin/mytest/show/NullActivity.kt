package rango.kotlin.mytest.show

import android.app.Activity
import android.os.Bundle
import android.util.Log
import rango.kotlin.mytest.share.HttpData
import rango.tool.androidtool.R

class NullActivity : Activity() {

    private var httpData: HttpData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_null_layout)

        httpData?.getValue()
        httpData?.getAge()
        httpData?.getHeight()
        httpData?.isMarried()
        httpData?.isRegister()


//       val isRegister:Boolean? =  httpData?.let {
//            it.getValue()
//            it.getHeight()
//            it.getAge()
//            it.isRegister()
//        }
//
//        val isMarried: Boolean? = httpData?.run {
//            getValue()
//            getHeight()
//            getAge()
//            isRegister()
//            isMarried()
//        }


//        val name:HttpData? = httpData?.also {
//            it.getHeight()
//            it.getAge()
//            it.isMarried()
//            it.getName()
//        }

       val data:HttpData? =  httpData?.apply {
            getHeight()
            isMarried()
            getName()
        }

        with(httpData){
            this?.getHeight()
        }

    }
}
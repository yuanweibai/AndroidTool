package rango.kotlin

import rango.kotlin.mytest.BaseImpl
import rango.kotlin.mytest.Diver
import rango.kotlin.mytest.MyTest
import rango.tool.androidtool.java.HaHa


fun process(test: MyTest) {
    test.run()
}

fun main(args: Array<String>) {
    println("main")

//    var x = 8
//    process(object : MyTest() {
//        override fun run() {
//            if (x == 8) {
//                print("dd")
//                x = 89
//            } else {
//                print("dadfa")
//            }
//        }
//    })
//
//    MyTest.create()
//
//    Diver(BaseImpl(5)).base()

}

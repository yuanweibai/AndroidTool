package rango.kotlin

import rango.kotlin.mytest.BaseImpl
import rango.kotlin.mytest.Diver
import rango.kotlin.mytest.MyTest
import rango.tool.androidtool.java.HaHa


fun process(test: MyTest) {
    test.run()
}


fun main(args: Array<String>) {
    val array = intArrayOf(134, 4, 290877, 43, 9, 130, 310, 56, 1, 19)
    println("-----------排序----------")

    println("1. 冒泡排序")

    var isExchange: Boolean
    var lastExchangePosition: Int = array.size - 1
    for (i in array.size - 1 downTo 0) {
        isExchange = false
        val last = if (i < lastExchangePosition) i else lastExchangePosition
        for (k in 0 until last) {
            if (array[k] > array[k + 1]) {
                val temp = array[k]
                array[k] = array[k + 1]
                array[k + 1] = temp
                isExchange = true
                lastExchangePosition = k
            }
        }

        if (!isExchange) {
            break
        }
    }

    array.forEach {
        println(it)
    }

}

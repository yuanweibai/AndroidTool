package rango.kotlin

import rango.kotlin.mytest.BaseImpl
import rango.kotlin.mytest.Diver
import rango.kotlin.mytest.MyTest
import rango.tool.androidtool.java.HaHa
import kotlin.math.sqrt


fun process(test: MyTest) {
    test.run()
}


fun main(args: Array<String>) {
    val array = intArrayOf(-8, 9, 0, 134, 8, 4, 290877, 8, 43, 8, 9, 130, 8, 310, 56, 1, 8, 19)

    quickly(array, 0, array.size - 1)
    array.forEach {
        println("$it")
    }

}

fun quickly(array: IntArray, left: Int, right: Int) {
    if (left >= right) {
        return
    }

    val standard = array[left]
    var l = left
    var r = right

    while (l < r) {
        while (array[r] >= standard && r > l) {
            r--
        }
        while (array[l] <= standard && l < r) {
            l++
        }

        if (l == r){
            break
        }

        val temp = array[l]
        array[l] = array[r]
        array[r] = temp
    }
    // 最后的 l 与 r 必然是相等的
    // array[l] 必然是小于 standard 的，因为在上述循环中，我们是先判断大于 standard 的，因此最后的 r 下标的值必然是小于 standard 的。
    array[left] = array[l]
    array[l] = standard
    quickly(array, left, l - 1)
    quickly(array, l + 1, right)
}



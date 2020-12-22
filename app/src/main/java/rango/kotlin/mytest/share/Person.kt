package rango.kotlin.mytest.share

import android.util.Log

open class Person(name: String) {

    constructor(name: String, age: Int) : this(name) {
        println("2")
    }

    init {
        println("3---name = $name")
    }

    open fun run(speed: Int) {

    }

    fun eat(food: String) {

    }
}
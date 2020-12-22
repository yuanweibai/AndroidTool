package rango.kotlin.mytest.share

import android.util.Log

open class Person(name: String) {

    constructor(name: String, age: Int) : this(name) {

    }

    init {

    }

    open fun run(speed: Int) {

    }

    fun eat(food:String){

    }
}
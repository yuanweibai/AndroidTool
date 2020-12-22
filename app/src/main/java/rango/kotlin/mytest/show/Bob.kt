package rango.kotlin.mytest.show

import rango.kotlin.mytest.share.Person

class Bob(age: Int) : Person("Bob", age) {

    constructor(isMarried: Boolean, age: Int) : this(age) {
        println("4")
    }

    init {
        println("5---age = $age")
    }

    override fun run(speed: Int) {
        super.run(speed)
    }
}
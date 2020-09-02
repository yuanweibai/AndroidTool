package rango.kotlin.mytest

class BaseImpl(val x: Int) : Base {

    override fun base() {
        println("baseImpl+${x}")
    }

}
package rango.kotlin.mytest

class Diver(b: Base) : Base by b {

    fun d() {
        println("Diver-----d")
    }
}
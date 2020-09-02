package rango.kotlin.mytest

open class MyTest {

    companion object {
        fun create() {
            println()
        }
    }

    private var name:String
        get():String {
            println("MyTest get is run....")
            return name
        }
        set(na: String) {
            println("MyTest set is run.....")
            name = na
        }

    private fun foo() = object {
        val x = 9
        val h = 7
    }

    fun hhh() = object {
        val y = 8
    }

    open fun run() {
        print("test")
    }

}
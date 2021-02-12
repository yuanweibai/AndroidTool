package rango.kotlin.mytest

import androidx.databinding.*

class People(name: ObservableField<String>, age: ObservableInt, msg: ObservableField<String>) : BaseObservable() {

    val nameObservable: ObservableField<String> = name
    val ageObservable: ObservableInt = age
    val msgObservable: ObservableField<String> = msg

}
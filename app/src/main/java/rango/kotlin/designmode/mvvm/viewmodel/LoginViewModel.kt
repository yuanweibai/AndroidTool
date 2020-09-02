package rango.kotlin.designmode.mvvm.viewmodel

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.databinding.*
import rango.tool.androidtool.ToolApplication
import rango.tool.androidtool.databinding.LoginBinding

class LoginViewModel {

    var nameObservable = ObservableField("")
    var passwordObservable = ObservableField("")
    var ageObservable = ObservableInt(0)
    var btnClickable = ObservableBoolean(false)

    val nameWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            btnClickable.set(isClickable())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    val passwordWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            btnClickable.set(isClickable())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    fun isClickable(): Boolean {
        return !TextUtils.isEmpty(nameObservable.get()) && !TextUtils.isEmpty(passwordObservable.get())
    }


    fun clickTitleImage(view: View) {
        Toast.makeText(ToolApplication.getContext(), "clickTitleImage, isClickable = ${isClickable()}, width = ${view.width}, height = ${view.height}", Toast.LENGTH_LONG).show()
    }

    fun clickLoginBtn() {
        Toast.makeText(ToolApplication.getContext(), "name = ${nameObservable.get()}, password = ${passwordObservable.get()}", Toast.LENGTH_LONG).show()
    }


}
package rango.kotlin.wanandroid.common.utils

import android.app.Application

object WAApplication {

    lateinit var application: Application

    fun attachBaseContext(application: Application) {
        this.application = application
    }
}
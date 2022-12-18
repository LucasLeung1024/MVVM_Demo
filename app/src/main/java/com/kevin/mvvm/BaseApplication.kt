package com.kevin.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 自定义 Application
 */
class BaseApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        //全局context
        context = applicationContext
    }

}

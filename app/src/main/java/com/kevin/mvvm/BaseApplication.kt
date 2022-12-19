package com.kevin.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kevin.mvvm.db.AppDatabase
import com.kevin.mvvm.utils.MVUtils
import com.tencent.mmkv.MMKV

/**
 * 自定义 Application
 */
class BaseApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //数据库
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        //全局context
        context = applicationContext
        //MMKV初始化
        MMKV.initialize(this)
        //工具类初始化
        MVUtils.getInstance()
        //创建本地数据库
        db = AppDatabase.getInstance(context)
    }

}

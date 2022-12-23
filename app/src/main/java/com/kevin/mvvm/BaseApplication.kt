package com.kevin.mvvm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.kevin.mvvm.ui.activity.ActivityManager
import com.kevin.mvvm.db.AppDatabase
import com.kevin.mvvm.utils.MVUtils
import com.tencent.mmkv.MMKV
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk

/**
 * 自定义 Application
 */
class BaseApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //数据库
        lateinit var db: AppDatabase
        fun getActivityManager(): ActivityManager? {
            return ActivityManager.mInstance
        }
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
        //腾讯WebView初始化
        initX5WebView()
    }

    private fun initX5WebView() {
        val map = mutableMapOf<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {}
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)
    }

}
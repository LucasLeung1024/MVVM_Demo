package com.kevin.mvvm.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kevin.mvvm.BaseApplication

/**
 * 基础Activity
 */
open class BaseActivity : AppCompatActivity() {
    protected var context: AppCompatActivity? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        BaseApplication.getActivityManager()?.addActivity(this);
        super.onCreate(savedInstanceState)
        context = this
    }

    protected fun showMsg(msg: CharSequence?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun showLongMsg(msg: CharSequence?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * 跳转页面
     * @param clazz 目标页面
     */
    protected fun jumpActivity(clazz: Class<*>?) {
        startActivity(Intent(context, clazz))
    }

    /**
     * 跳转页面并关闭当前页面
     * @param clazz 目标页面
     */
    protected fun jumpActivityFinish(clazz: Class<*>?) {
        startActivity(Intent(context, clazz))
        finish()
    }

    protected open fun back(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }
    }

    /**
     * 状态栏文字图标颜色
     * @param dark 深色 false 为浅色
     */
    protected fun setStatusBar(dark: Boolean) {
        val decor: View = window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    protected open fun exitTheProgram() {
        BaseApplication.getActivityManager()!!.finishAllActivity()
    }


}

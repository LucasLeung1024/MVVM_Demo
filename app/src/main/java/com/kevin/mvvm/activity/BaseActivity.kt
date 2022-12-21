package com.kevin.mvvm.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.utils.PermissionUtils
import com.kevin.mvvm.view.dialog.LoadingDialog


/**
 * 基础Activity
 */
open class BaseActivity : AppCompatActivity() {

    /**
     * 打开相册请求码
     */
    protected val SELECT_PHOTO_CODE = 2000

    /**
     * 打开相机请求码
     */
    protected val TAKE_PHOTO_CODE = 2001


    protected var context: AppCompatActivity? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        BaseApplication.getActivityManager()?.addActivity(this);
        PermissionUtils.getInstance()
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

    private var loadingDialog: LoadingDialog? = null

    /**
     * 显示加载弹窗
     */
    protected open fun showLoading() {
        loadingDialog = LoadingDialog(this)
        loadingDialog!!.show()
    }

    /**
     * 显示加载弹窗
     *
     * @param isClose true 则点击其他区域弹窗关闭， false 不关闭。
     */
    protected open fun showLoading(isClose: Boolean) {
        loadingDialog = LoadingDialog(this, isClose)
        loadingDialog!!.show()
    }

    /**
     * 隐藏加载弹窗
     */
    protected open fun dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }
    }

    /**
     * 当前是否在Android11.0及以上
     */
    protected open fun isAndroid11(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    /**
     * 当前是否在Android10.0及以上
     */
    protected open fun isAndroid10(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 当前是否在Android7.0及以上
     */
    protected open fun isAndroid7(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * 当前是否在Android6.0及以上
     */
    protected open fun isAndroid6(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    @RequiresApi(Build.VERSION_CODES.R)
    protected open fun isStorageManager(): Boolean {
        return Environment.isExternalStorageManager()
    }

    protected open fun hasPermission(permissionName: String?): Boolean {
        return PermissionUtils.hasPermission(this, permissionName)
    }

    protected open fun requestPermission(permissionName: String?) {
        PermissionUtils.requestPermission(this, permissionName)
    }

    /**
     * 请求外部存储管理 Android11版本时获取文件读写权限时调用
     */
    protected open fun requestManageExternalStorage() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE)
    }

}

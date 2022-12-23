package com.kevin.mvvm.ui.activity

import android.app.Activity

//退出这是一个需要小心的功能，因为涉及到Activity的栈，当我们从一个Activity跳转到另一个Activity时，
// 如果之前的Activity没有销毁掉，则它就在栈里，当前跳转的Activity在栈顶。而不可能每一次跳转页面都
// 需要销毁之前的页面。因此当应用需要退出时，首先我们应该销毁掉所有的Activity，然后再去关掉进程，这样
// 你的程序才算是完整退出了。

class ActivityManager {
    //保存所有创建的Activity
    private val activityList: MutableList<Activity> = ArrayList()

    /**
     * 添加Activity
     * @param activity
     */
    fun addActivity(activity: Activity?) {
        if (activity != null) {
            activityList.add(activity)
        }
    }

    /**
     * 移除Activity
     * @param activity
     */
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            activityList.remove(activity)
        }
    }

    /**
     * 关闭所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityList) {
            activity.finish()
        }
    }

    companion object {
        var mInstance: ActivityManager? = null
        val instance: ActivityManager?
            get() {
                if (mInstance == null) {
                    synchronized(ActivityManager::class.java) {
                        if (mInstance == null) {
                            mInstance = ActivityManager()
                        }
                    }
                }
                return mInstance
            }
    }
}

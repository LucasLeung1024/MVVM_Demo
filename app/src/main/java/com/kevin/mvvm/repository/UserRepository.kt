package com.kevin.mvvm.repository

import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.db.bean.User
import kotlinx.coroutines.Dispatchers

object UserRepository: BaseRepository() {

    private val TAG = UserRepository::class.java.simpleName

    fun getUser() = fire(Dispatchers.IO) {
        var returnUser: User? = null
        val userList = BaseApplication.db.userDao().getAll()
        if (userList.isNotEmpty()) {
            for (user in userList) {
                if (user.uid == 1) {
                    returnUser = user
                }
            }
        }
        Result.success(returnUser)
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    fun updateUser(user: User) {
        BaseApplication.db.userDao().update(user)
    }


    /**
     * 保存热门壁纸数据
     */
    fun saveUser(user: User?) {
        BaseApplication.db.userDao().deleteAll()
        BaseApplication.db.userDao().insert(user!!)
    }

}
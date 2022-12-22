package com.kevin.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.kevin.mvvm.db.bean.User
import com.kevin.mvvm.repository.UserRepository.saveUser


class RegisterViewModel : ViewModel()
{
    var user: MutableLiveData<User> = MutableLiveData()

    /**
     * 注册
     */
    fun register() {
        user.value?.uid = 1
        Log.d("TAG", "register: " + Gson().toJson(user.value))
        saveUser(user.value!!)
    }


}
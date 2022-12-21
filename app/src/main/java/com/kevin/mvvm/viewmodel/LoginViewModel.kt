package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.db.bean.User
import com.kevin.mvvm.repository.UserRepository

class LoginViewModel:ViewModel() {

    var user: MutableLiveData<User> = MutableLiveData()

    fun getLocalUser() = UserRepository.getUser()
}
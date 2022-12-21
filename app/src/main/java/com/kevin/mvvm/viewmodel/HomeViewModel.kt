package com.kevin.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.db.bean.User
import com.kevin.mvvm.repository.UserRepository


class HomeViewModel:ViewModel() {

    var user =  UserRepository.getUser()

    var defaultName = "初学者-Study"
    var defaultIntroduction = "Android | Java"

    fun getUser() {
        user = UserRepository.getUser()
    }

    fun updateUser(user: User?) {
        UserRepository.updateUser(user!!)
        getUser()
    }

}
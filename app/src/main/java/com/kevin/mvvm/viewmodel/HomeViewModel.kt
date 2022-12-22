package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.db.bean.User
import com.kevin.mvvm.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel:ViewModel() {

    var user: MutableLiveData<User>? = MutableLiveData()

    var defaultName = "初学者-Study"
    var defaultIntroduction = "Android | Java"

    fun getUser(){
        user!!.value = UserRepository.getUser2()
    }

    fun updateUser(user2: User?) = runBlocking{
        launch(Dispatchers.IO) {
            UserRepository.updateUser(user2!!)
            user!!.postValue(user2)
        }
    }

}
package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.model.User

class MainViewModel:ViewModel() {

    var user: MutableLiveData<User> = MutableLiveData()

//    fun getuser(): MutableLiveData<User> {
//        if (user == null) {
//            user = MutableLiveData()
//        }
//        return user as MutableLiveData<User>
//    }

}
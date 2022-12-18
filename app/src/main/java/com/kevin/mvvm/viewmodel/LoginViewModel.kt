package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.model.User

class LoginViewModel:ViewModel() {

    var user: MutableLiveData<User> = MutableLiveData()

}
package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.repository.WebRepository

class WebViewModel:ViewModel() {

    private val uniquekeyLiveData = MutableLiveData<String>()

    fun setUniqueKey(key:String){
        uniquekeyLiveData.value = key
    }
    var newsDetailsResponse = Transformations.switchMap(uniquekeyLiveData){ query->
        WebRepository.getNewsDetail(query)
    }

}
package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.mvvm.model.BiYingResponse
import com.kevin.mvvm.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    var biying: MutableLiveData<Result<BiYingResponse>> = MutableLiveData()

    fun getBiYing() {
        viewModelScope.launch {
            val result = try {
                // 网络返回成功
                Result.success(MainRepository.getBiYing())
            } catch (e: Exception) {
                // 网络返回失败
                Result.failure(e)
            }
            // 发射数据，之后观察者就会收到数据
            // 注意这里是主线程，直接用setValue()即可
            biying.value = result
        }
    }

}
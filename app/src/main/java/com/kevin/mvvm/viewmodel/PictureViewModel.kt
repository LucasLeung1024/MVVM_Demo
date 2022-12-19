package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.mvvm.db.bean.WallPaper
import com.kevin.mvvm.repository.PictureRepository
import kotlinx.coroutines.launch

class PictureViewModel:ViewModel() {

    var wallPaperList: MutableLiveData<Result<MutableList<WallPaper>>> = MutableLiveData()

    fun getWallPaper() {
        viewModelScope.launch {
            val result = Result.success(PictureRepository.getWallPaper())
            // 发射数据，之后观察者就会收到数据，放在子线程中
            wallPaperList.postValue(result)
        }
    }

}
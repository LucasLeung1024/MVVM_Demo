package com.kevin.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kevin.mvvm.repository.MainRepository

class MainViewModel:ViewModel() {

    var biying = MainRepository.getBiYing()

    var wallPaper = MainRepository.getWallPaper()

}
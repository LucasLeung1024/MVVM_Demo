package com.kevin.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kevin.mvvm.repository.NewsRepository

class NewsViewModel : ViewModel() {
    var news = NewsRepository.getNews()
}
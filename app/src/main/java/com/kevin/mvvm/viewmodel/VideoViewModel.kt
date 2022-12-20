package com.kevin.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kevin.mvvm.repository.VideoRepository

class VideoViewModel : ViewModel() {
    var videos = VideoRepository.getVideos()
}
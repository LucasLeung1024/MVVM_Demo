package com.kevin.mvvm.repository

import android.util.Log
import com.kevin.mvvm.model.BiYingResponse
import kotlinx.coroutines.Dispatchers
import com.kevin.mvvm.network.NetworkRequest

object MainRepository: BaseRepository() {

    suspend fun getBiYing():BiYingResponse {
        val dailyPic = NetworkRequest.getDailyPic()
        Log.d("getBiYing success","$dailyPic")
        return dailyPic
    }

}
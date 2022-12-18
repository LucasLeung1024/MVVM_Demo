package com.kevin.mvvm.network


import com.kevin.mvvm.model.BiYingResponse
import retrofit2.Call
import retrofit2.http.GET

interface DailyImageService {

    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    fun getDailyPic(): Call<BiYingResponse>

}
package com.kevin.mvvm.network


import com.kevin.mvvm.model.BiYingResponse
import com.kevin.mvvm.model.WallPaperResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    fun getDailyPic(): Call<BiYingResponse>

    /**
     * 热门壁纸
     */
    @GET("/v1/vertical/vertical?limit=30&skip=180&adult=false&first=0&order=hot")
    fun wallPaper(): Call<WallPaperResponse>

}
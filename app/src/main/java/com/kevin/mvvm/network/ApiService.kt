package com.kevin.mvvm.network

import com.kevin.mvvm.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

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

    /**
     * 聚合新闻数据
     */
    @GET("/toutiao/index?type=&page=&page_size=&is_filter=&key=99d3951ed32af2930afd9b38293a08a2")
    fun news(): Call<NewsResponse>

    /**
     * 聚合热门视频数据
     */
    @GET("/fapig/douyin/billboard?type=hot_video&size=20&key=a9c49939cae34fc7dae570b1a4824be4")
    fun video(): Call<VideoResponse>

    /**
     * 聚合新闻数据详情
     */
    @GET("/toutiao/content?key=99d3951ed32af2930afd9b38293a08a2")
    fun newsDetail(@Query("uniquekey") uniquekey: String?): Call<NewsDetailResponse>

}
package com.kevin.mvvm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit构建器
object ServiceCreator4 {

    private var BASE_URL: String? = "https://cn.bing.com" //必应
    private var BASE_URL2: String? = "http://service.picasso.adesk.com"  //热门壁纸
    private var BASE_URL3: String? = "http://v.juhe.cn" //聚合API 1  请求新闻数据
    private var BASE_URL4: String? = "http://apis.juhe.cn" //聚合API 2  请求视频数据

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL4)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)

}




package com.kevin.mvvm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit构建器
object ServiceCreator2 {

    private var BASE_URL: String? = "https://cn.bing.com" //必应
    private var BASE_URL2: String? = "http://service.picasso.adesk.com"  //热门壁纸

    //热门壁纸使用的
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL2)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)


}




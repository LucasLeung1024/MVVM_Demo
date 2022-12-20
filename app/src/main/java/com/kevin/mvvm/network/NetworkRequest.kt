package com.kevin.mvvm.network

import com.kevin.mvvm.model.BiYingResponse
import com.kevin.mvvm.model.NewsResponse
import com.kevin.mvvm.model.VideoResponse
import com.kevin.mvvm.model.WallPaperResponse
import com.kevin.mvvm_5.network.ServiceCreator3
import com.kevin.mvvm_5.network.ServiceCreator4
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//统一的数据源访问入口，对所有网络请求的API进行封装
object NetworkRequest {
    /**
     * 创建服务
     */
    private val dailyPicService = ServiceCreator.create(ApiService::class.java)
    private val wallPaperService = ServiceCreator2.create(ApiService::class.java)
    private val newsService = ServiceCreator3.create(ApiService::class.java)
    private val videosService = ServiceCreator4.create(ApiService::class.java)

    //通过await()函数将getDailyPic()函数也声明成挂起函数。使用协程
    suspend fun getDailyPic(): BiYingResponse = dailyPicService.getDailyPic().await()
    suspend fun getWallPaper(): WallPaperResponse = wallPaperService.wallPaper().await()
    suspend fun getNews(): NewsResponse = newsService.news().await()
    suspend fun getVideos(): VideoResponse = videosService.video().await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                //正常返回
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
                //异常返回
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}
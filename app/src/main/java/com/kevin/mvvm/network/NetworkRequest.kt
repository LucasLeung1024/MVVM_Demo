package com.kevin.mvvm.network

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
    private val dailyImageService = ServiceCreator.create(DailyImageService::class.java)

    //通过await()函数将getDailyPic()函数也声明成挂起函数。使用协程
    suspend fun getDailyPic() = dailyImageService.getDailyPic().await()

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
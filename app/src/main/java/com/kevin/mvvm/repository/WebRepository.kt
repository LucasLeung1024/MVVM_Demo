package com.kevin.mvvm.repository

import android.util.Log
import com.kevin.mvvm.model.NewsDetailResponse
import com.kevin.mvvm.network.NetworkRequest
import kotlinx.coroutines.Dispatchers

object WebRepository: BaseRepository() {

    private val TAG = WebRepository::class.java.simpleName

    private lateinit var newsDetailResponse: NewsDetailResponse

    /**
     * 获取新闻详情数据
     * @param uniquekey 新闻ID
     * @return newsDetail
     */
    fun getNewsDetail(uniquekey: String) =  WebRepository.fire(Dispatchers.Main){
        newsDetailResponse = NetworkRequest.getNewsDetail(uniquekey)
        if (newsDetailResponse.error_code == 0) {
            Log.d(TAG, "网络获取成功")
            Result.success(newsDetailResponse)
        } else {
            Log.d(TAG, "网络获取失败")
            Result.failure(RuntimeException("News Error"))
        }
    }

}
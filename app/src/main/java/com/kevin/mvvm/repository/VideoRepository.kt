package com.kevin.mvvm.repository

import android.util.Log
import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.db.bean.Video
import com.kevin.mvvm.model.VideoResponse
import com.kevin.mvvm.network.NetworkRequest
import com.kevin.mvvm.utils.Constant
import com.kevin.mvvm.utils.EasyDate
import com.kevin.mvvm.utils.MVUtils
import kotlinx.coroutines.Dispatchers


object VideoRepository: BaseRepository() {
    private val TAG = VideoRepository::class.java.simpleName

    private lateinit var videoResponse: VideoResponse

    fun getVideos() = fire(Dispatchers.IO){
        //今日此接口是否已请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST_VIDEO)) {
            if (EasyDate.timestamp <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP)) {
                //当前时间未超过次日0点，从本地获取
                videoResponse = getVideoForLocalDB()
            } else {
                //大于则数据需要更新，从网络获取
                videoResponse = NetworkRequest.getVideos()
                //保存到本地数据库
                saveVideo(videoResponse)
            }
        } else{
            //没有请求过接口 或 当前时间，从网络获取
            videoResponse = NetworkRequest.getVideos()
            //存储到本地数据库中，并记录今日已请求了数据
            saveVideo(videoResponse)
        }
        if (videoResponse.reason  == "查询成功") {
            Result.success(videoResponse)
        } else {
            Result.failure(RuntimeException("News Error"))
        }
    }


    /**
     * 保存到本地数据库
     */
    private fun saveVideo(videoResponse: VideoResponse) {
        //记录今日已请求
        MVUtils.put(Constant.IS_TODAY_REQUEST_VIDEO, true)
        //记录此次请求的时最晚有效时间戳
        MVUtils.put(Constant.REQUEST_TIMESTAMP, EasyDate.getMillisNextEarlyMorning())
        BaseApplication.db.videoDao().deleteAll()
        Log.d(TAG, "saveVideo: 删除数据成功");
        val videoList: MutableList<Video> = ArrayList()
        for (resultBean in videoResponse.result!!) {
            videoList.add(
                Video(
                    resultBean.title,
                    resultBean.share_url,
                    resultBean.author,
                    resultBean.item_cover,
                    resultBean.hot_words
                )
            )
        }
        //保存到数据库
        BaseApplication.db.videoDao().insertAll(videoList)
        Log.d(TAG, "saveVideos: 插入视频：" + videoList.size + "条")
    }

    /**
     * 从本地数据库获取热门壁纸
     */
    private suspend fun getVideoForLocalDB() :VideoResponse {
        Log.d(TAG, "getVideoForLocalDB: 从本地数据库获取 视频数据")
        val videoResponse = VideoResponse()

        val dataBeanList: MutableList<VideoResponse.ResultBean> = ArrayList()
        val videList = BaseApplication.db.videoDao().getAll()
        for (video in videList) {
            val resultBean = VideoResponse.ResultBean()
            resultBean.title = video.title
            resultBean.share_url = video.share_url
            resultBean.author = video.author
            resultBean.hot_words = video.hot_words
            resultBean.item_cover = video.item_cover
            dataBeanList.add(resultBean)
        }
        videoResponse.result = dataBeanList
        videoResponse.reason = "查询成功"
        return videoResponse
    }

}
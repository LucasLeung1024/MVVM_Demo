package com.kevin.mvvm.repository

import android.util.Log
import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.db.bean.Image
import com.kevin.mvvm.model.BiYingResponse
import com.kevin.mvvm.network.NetworkRequest
import com.kevin.mvvm.utils.Constant
import com.kevin.mvvm.utils.EasyDate
import com.kevin.mvvm.utils.MVUtils
import kotlinx.coroutines.Dispatchers

object MainRepository: BaseRepository() {

    private val TAG = MainRepository::class.java.simpleName

    lateinit var dailyPic: BiYingResponse

    suspend fun getBiYing():BiYingResponse {
    //今日此接口是否已请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST)) {
            if (EasyDate.timestamp <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP)) {
                //当前时间未超过次日0点，从本地获取
                dailyPic = getLocalDB()
            } else {
                //大于则数据需要更新，从网络获取
                dailyPic = NetworkRequest.getDailyPic()
                Log.d(TAG, "从网络中获取1")
                dailyPic = NetworkRequest.getDailyPic()
                //保存到本地数据库
                savPics(dailyPic)
            }
        } else{
            Log.d(TAG, "从网络中获取2")
            //没有请求过接口 或 当前时间，从网络获取
            dailyPic = NetworkRequest.getDailyPic()
            //存储到本地数据库中，并记录今日已请求了数据
            savPics(dailyPic)
        }
//        if (dailyPic.images?.isNotEmpty() == true) {
//            Result.success(dailyPic)
//        } else {
//            Result.failure(RuntimeException("BiYing Error"))
//        }
        return dailyPic
    }


    /**
     * 保存到本地数据库
     */
    private suspend fun savPics(biYingResponse: BiYingResponse) {
        //记录今日已请求
        MVUtils.put(Constant.IS_TODAY_REQUEST, true)
        //记录此次请求的时最晚有效时间戳
        MVUtils.put(Constant.REQUEST_TIMESTAMP, EasyDate.getMillisNextEarlyMorning())
        val bean: BiYingResponse.ImagesBean = biYingResponse.images!![0]
        val image = Image(1, bean.url, bean.urlbase, bean.copyright, bean.copyrightlink, bean.title)
        //BaseApplication.db.imageDao().deleteAll()
        BaseApplication.db.imageDao().insertAll(image)
    }


    private suspend fun getLocalDB(): BiYingResponse {
        Log.d(TAG, "从数据库中获取")
        val image = BaseApplication.db.imageDao().queryById(1)
        val response = BiYingResponse()

        //从数据库度数并拼接数据
        val imagesBean = BiYingResponse.ImagesBean()
        imagesBean.url = image.url
        imagesBean.urlbase = image.urlbase
        imagesBean.copyright =  image.copyright
        imagesBean.copyrightlink = image.copyrightlink
        imagesBean.title = image.title
        val imagesBeanList: MutableList<BiYingResponse.ImagesBean> = ArrayList()
        imagesBeanList.add(imagesBean)
        response.images = imagesBeanList

        return response
    }

}
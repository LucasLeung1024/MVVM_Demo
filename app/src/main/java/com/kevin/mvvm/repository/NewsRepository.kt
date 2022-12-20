package com.kevin.mvvm.repository

import android.util.Log
import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.db.bean.News
import com.kevin.mvvm.model.NewsResponse
import com.kevin.mvvm.model.NewsResponse.ResultBean.DataBean
import com.kevin.mvvm.network.NetworkRequest
import com.kevin.mvvm.utils.Constant
import com.kevin.mvvm.utils.EasyDate
import com.kevin.mvvm.utils.MVUtils
import kotlinx.coroutines.Dispatchers


object NewsRepository: BaseRepository() {
    private val TAG = NewsRepository::class.java.simpleName

    lateinit var newsResponse: NewsResponse

    fun getNews() = fire(Dispatchers.IO){
        //今日此接口是否已请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST_NEWS)) {
            if (EasyDate.timestamp <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP)) {
                //当前时间未超过次日0点，从本地获取
                newsResponse = getNewsForLocalDB()
            } else {
                //大于则数据需要更新，从网络获取
                newsResponse = NetworkRequest.getNews()
                //保存到本地数据库
                saveNews(newsResponse)
            }
        } else{
            //没有请求过接口 或 当前时间，从网络获取
            newsResponse = NetworkRequest.getNews()
            //存储到本地数据库中，并记录今日已请求了数据
            saveNews(newsResponse)
        }
        if (newsResponse.reason  == "success!") {
            Log.d(TAG, "网络获取成功")
            Result.success(newsResponse)
        } else {
            Log.d(TAG, "网络获取失败")
            Result.failure(RuntimeException("News Error"))
        }
    }


    /**
     * 保存到本地数据库
     */
    private fun saveNews(newsResponse: NewsResponse) {
        //记录今日已请求
        MVUtils.put(Constant.IS_TODAY_REQUEST_NEWS, true)
        //记录此次请求的时最晚有效时间戳
        MVUtils.put(Constant.REQUEST_TIMESTAMP, EasyDate.getMillisNextEarlyMorning())
        BaseApplication.db.newsDao().deleteAll()
        Log.d(TAG, "saveNews: 删除数据成功")
        val newsList: MutableList<News> = ArrayList()
        for (dataBean in newsResponse.result!!.data!!) {
            newsList.add(
                News(
                    dataBean.uniquekey,
                    dataBean.title,
                    dataBean.date,
                    dataBean.category,
                    dataBean.author_name,
                    dataBean.url,
                    dataBean.thumbnail_pic_s,
                    dataBean.is_content
                )
            )
        }
        //保存到数据库
        BaseApplication.db.newsDao().insertAll(newsList)
        Log.d(TAG, "saveNews: 插入数据：" + newsList.size + "条")
    }

    /**
     * 从本地数据库获取热门壁纸
     */
    private suspend fun getNewsForLocalDB() :NewsResponse {
        Log.d(TAG, "getNewsForLocalDB: 从本地数据库获取 新闻数据")
        val newsResponse = NewsResponse()
        val resultBean = NewsResponse.ResultBean()

        val dataBeanList: MutableList<DataBean> = ArrayList()
        val newsList: List<News> = BaseApplication.db.newsDao().getAll()
        for (news1 in newsList) {
            val dataBean = DataBean()
            dataBean.uniquekey = news1.uniquekey
            dataBean.title = news1.title
            dataBean.date = news1.date
            dataBean.author_name = news1.author_name
            dataBean.category = news1.category
            dataBean.thumbnail_pic_s = news1.thumbnail_pic_s
            dataBean.is_content = news1.content
            dataBeanList.add(dataBean)
        }
        resultBean.data = dataBeanList
        newsResponse.result = resultBean
        newsResponse.reason = "success!"
        return newsResponse
    }


}
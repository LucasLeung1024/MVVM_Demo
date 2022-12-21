package com.kevin.mvvm.model

//点开新闻详情
class NewsDetailResponse {
    var reason: String? = null
    var result: ResultBean? = null
    var error_code: Int? = null

    class ResultBean {
        var uniquekey: String? = null
        var detail: DetailBean? = null
        var content: String? = null

        class DetailBean {
            var title: String? = null
            var date: String? = null
            var category: String? = null
            var author_name: String? = null
            var url: String? = null
            var thumbnail_pic_s: String? = null
            var thumbnail_pic_s02: String? = null
            var thumbnail_pic_s03: String? = null
        }
    }
}


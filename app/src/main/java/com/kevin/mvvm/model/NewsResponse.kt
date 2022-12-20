package com.kevin.mvvm.model

class NewsResponse {
    var reason: String? = null
    var result: ResultBean? = null
    var error_code = 0

    class ResultBean {
        var stat: String? = null
        var data: MutableList<DataBean>? = null
        var page: String? = null
        var pageSize: String? = null

        class DataBean {
            var uniquekey: String? = null
            var title: String? = null
            var date: String? = null
            var category: String? = null
            var author_name: String? = null
            var url: String? = null
            var thumbnail_pic_s: String? = null
            var is_content: String? = null
            var thumbnail_pic_s02: String? = null
            var thumbnail_pic_s03: String? = null
        }
    }
}

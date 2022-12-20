package com.kevin.mvvm.model

class VideoResponse {
    var reason: String? = null
    var result: MutableList<ResultBean>? = null
    var error_code: Int? = null

    class ResultBean {
        var title: String? = null
        var share_url: String? = null
        var author: String? = null
        var item_cover: String? = null
        var hot_value: Int? = null
        var hot_words: String? = null
        var play_count: Int? = null
        var digg_count: Int? = null
        var comment_count: Int? = null
    }
}

package com.kevin.mvvm.model

/**
 * 必应访问接口返回数据实体
 * @description BiYingImgResponse
 */
class BiYingResponse {
    var tooltips: TooltipsBean? = null
    var images: List<ImagesBean>? = null

    class TooltipsBean {
        var loading: String? = null
        var previous: String? = null
        var next: String? = null
        var walle: String? = null
        var walls: String? = null
    }

    class ImagesBean {
        var startdate: String? = null
        var fullstartdate: String? = null
        var enddate: String? = null
        var url: String? = null
        var urlbase: String? = null
        var copyright: String? = null
        var copyrightlink: String? = null
        var title: String? = null
        var quiz: String? = null
        var wp = false
        var hsh: String? = null
        var drk = 0
        var top = 0
        var bot = 0
        var hs: List<*>? = null
    }
}

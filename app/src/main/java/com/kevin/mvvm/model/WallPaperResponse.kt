package com.kevin.mvvm.model

class WallPaperResponse {
    var msg: String? = null
    var res: ResBean? = null
    var code = 0

    class ResBean {
        var vertical: MutableList<VerticalBean>? = null

        class VerticalBean {
            var preview: String? = null
            var thumb: String? = null
            var img: String? = null
            var views = 0
            var rule: String? = null
            var ncos = 0
            var rank = 0
            var source_type: String? = null
            var wp: String? = null
            var isXr = false
            var isCr = false
            var favs = 0
            var atime = 0.0
            var id: String? = null
            var store: String? = null
            var desc: String? = null
            var cid: List<String>? = null
            var tag: List<*>? = null
            var url: List<*>? = null
        }
    }
}



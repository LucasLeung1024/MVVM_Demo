package com.kevin.mvvm.db.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Image {
    @PrimaryKey
    var uid = 0
    var url: String? = null
    var urlbase: String? = null
    var copyright: String? = null
    var copyrightlink: String? = null
    var title: String? = null

    constructor() {}

    @Ignore
    constructor(
        uid: Int,
        url: String?,
        urlbase: String?,
        copyright: String?,
        copyrightlink: String?,
        title: String?,
    ) {
        this.uid = uid
        this.url = url
        this.urlbase = urlbase
        this.copyright = copyright
        this.copyrightlink = copyrightlink
        this.title = title
    }
}

package com.kevin.mvvm.db.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class News {
    @PrimaryKey(autoGenerate = true)
    var uid = 0
    var uniquekey: String? = null
    var title: String? = null
    var date: String? = null
    var category: String? = null
    var author_name: String? = null
    var url: String? = null
    var thumbnail_pic_s: String? = null
    var content: String? = null

    constructor()

    @Ignore
    constructor(
        uniquekey: String?,
        title: String?,
        date: String?,
        category: String?,
        author_name: String?,
        url: String?,
        thumbnail_pic_s: String?,
        is_content: String?
    ) {
        this.uniquekey = uniquekey
        this.title = title
        this.date = date
        this.category = category
        this.author_name = author_name
        this.url = url
        this.thumbnail_pic_s = thumbnail_pic_s
        this.content = is_content
    }
}


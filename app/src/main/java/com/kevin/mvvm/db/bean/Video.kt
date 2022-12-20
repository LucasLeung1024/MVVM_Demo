package com.kevin.mvvm.db.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Video {
    @PrimaryKey(autoGenerate = true)
    var uid = 0
    var title: String? = null
    var share_url: String? = null
    var author: String? = null
    var item_cover: String? = null
    var hot_words: String? = null

    @Ignore
    constructor(
        title: String?,
        share_url: String?,
        author: String?,
        item_cover: String?,
        hot_words: String?
    ) {
        this.title = title
        this.share_url = share_url
        this.author = author
        this.item_cover = item_cover
        this.hot_words = hot_words
    }

    constructor() {}
}


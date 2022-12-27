package com.kevin.mvvm.db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notebook")
class Notebook {
    @PrimaryKey(autoGenerate = true)
    var uid = 0
    var title: String? = null
    var content: String? = null

    constructor(title: String?, content: String?) {
        this.title = title
        this.content = content
    }
}


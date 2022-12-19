package com.kevin.mvvm.db.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class WallPaper {
    @PrimaryKey(autoGenerate = true)
    var uid = 0
    var img: String? = null

    constructor()

    @Ignore
    constructor(img: String){
        this.img = img
    }
}

package com.kevin.mvvm.repository

import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.db.bean.WallPaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PictureRepository : BaseRepository() {

    var wallPaperList: MutableList<WallPaper> = arrayListOf()

    suspend fun getWallPaper(): MutableList<WallPaper> {
        withContext(Dispatchers.IO) {
            wallPaperList = BaseApplication.db.wallPaperDao().getAll() as MutableList<WallPaper>
        }
        return wallPaperList
    }

}

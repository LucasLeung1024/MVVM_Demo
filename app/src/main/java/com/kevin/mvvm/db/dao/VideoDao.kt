package com.kevin.mvvm.db.dao

import androidx.room.*
import com.kevin.mvvm.db.bean.Image
import com.kevin.mvvm.db.bean.News
import com.kevin.mvvm.db.bean.Video
import com.kevin.mvvm.db.bean.WallPaper


@Dao
interface VideoDao {
    @Query("SELECT * FROM video")
    suspend fun getAll(): List<Video>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<Video>)

    @Query("DELETE FROM video")
    fun deleteAll()
}

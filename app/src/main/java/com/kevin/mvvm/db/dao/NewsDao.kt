package com.kevin.mvvm.db.dao

import androidx.room.*
import com.kevin.mvvm.db.bean.Image
import com.kevin.mvvm.db.bean.News
import com.kevin.mvvm.db.bean.WallPaper


@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    suspend fun getAll(): List<News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<News>)

    @Query("DELETE FROM news")
    fun deleteAll()
}

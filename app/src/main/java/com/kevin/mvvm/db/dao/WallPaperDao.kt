package com.kevin.mvvm.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevin.mvvm.db.bean.WallPaper

@Dao
interface WallPaperDao {
    @Query("SELECT * FROM wallpaper")
    fun getAll(): List<WallPaper>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(wallPapers: List<WallPaper?>?)

    @Query("DELETE FROM wallpaper")
    fun deleteAll()
}

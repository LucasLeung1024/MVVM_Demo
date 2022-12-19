package com.kevin.mvvm.db.dao

import androidx.room.*
import com.kevin.mvvm.db.bean.Image

@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    suspend fun getAll(): List<Image>

    @Query("SELECT * FROM image WHERE uid LIKE :uid LIMIT 1")
    suspend fun queryById(uid: Int): Image

    @Insert
    suspend fun insertAll(images: Image)

    @Delete
    suspend fun delete(image: Image)

    @Query("DELETE FROM image")
    suspend fun deleteAll()
}

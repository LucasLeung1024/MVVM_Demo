package com.kevin.mvvm.db.dao

import androidx.room.*
import com.kevin.mvvm.db.bean.Notebook


@Dao
interface NotebookDao {
    @Query("SELECT * FROM notebook")
    suspend fun getAll(): List<Notebook>

    @Update
    fun update(notebook: Notebook)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notebook: Notebook)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<Notebook>)

    @Query("DELETE FROM notebook")
    fun deleteAll()

    @Delete
    fun delete(notebook: Notebook)

    @Query("SELECT * FROM notebook WHERE uid=:uid")
    fun findById(uid: Int): Notebook

}

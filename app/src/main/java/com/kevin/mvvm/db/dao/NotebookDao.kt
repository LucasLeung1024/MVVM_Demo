package com.kevin.mvvm.db.dao

import androidx.room.*
import com.kevin.mvvm.db.bean.Notebook


@Dao
interface NotebookDao {
    @Query("SELECT * FROM notebook")
    fun getAll(): MutableList<Notebook>

    @Update
    fun update(notebook: Notebook)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notebook: Notebook)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<Notebook>)

    @Query("DELETE FROM notebook")
    fun deleteAll()

    //@Delete  //这个是删除单个笔记的
    //fun delete(notebook: Notebook)

    //删除多个笔记，改成动态参数
    @Delete
    fun delete(vararg notebook: Notebook?)

    @Query("SELECT * FROM notebook WHERE uid=:uid")
    fun findById(uid: Int): Notebook

}

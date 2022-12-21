package com.kevin.mvvm.db.dao

import androidx.room.*
import com.kevin.mvvm.db.bean.User


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Update
    fun update(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(user: List<User>)

    @Query("DELETE FROM user")
    fun deleteAll()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}


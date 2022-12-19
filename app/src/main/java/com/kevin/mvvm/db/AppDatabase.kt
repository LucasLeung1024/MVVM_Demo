package com.kevin.mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kevin.mvvm.db.bean.Image
import com.kevin.mvvm.db.dao.ImageDao


@Database(entities = [Image::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val DATABASE_NAME = "mvvm_demo"

        /**
         * 单例模式
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .build().also { instance = it }
            }
        }
    }
}

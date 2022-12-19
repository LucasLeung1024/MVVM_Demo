package com.kevin.mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kevin.mvvm.db.bean.Image
import com.kevin.mvvm.db.bean.WallPaper
import com.kevin.mvvm.db.dao.ImageDao
import com.kevin.mvvm.db.dao.WallPaperDao

@Database(entities = [Image::class, WallPaper::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    abstract fun wallPaperDao(): WallPaperDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val DATABASE_NAME = "mvvm_demo"


        /**
         * 版本升级迁移
         */
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                    "CREATE TABLE `wallpaper` " +
                            "(uid INTEGER NOT NULL, " +
                            "img TEXT, " +
                            "PRIMARY KEY(`uid`))"
                )
            }
        }

        /**
         * 单例模式
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build().also { instance = it }
            }
        }
    }
}

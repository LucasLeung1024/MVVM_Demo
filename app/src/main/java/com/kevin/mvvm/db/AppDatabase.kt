package com.kevin.mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kevin.mvvm.db.bean.Image
import com.kevin.mvvm.db.bean.News
import com.kevin.mvvm.db.bean.Video
import com.kevin.mvvm.db.bean.WallPaper
import com.kevin.mvvm.db.dao.ImageDao
import com.kevin.mvvm.db.dao.NewsDao
import com.kevin.mvvm.db.dao.VideoDao
import com.kevin.mvvm.db.dao.WallPaperDao

@Database(entities = [Image::class, WallPaper::class, News::class, Video::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    abstract fun wallPaperDao(): WallPaperDao

    abstract fun videoDao(): VideoDao

    abstract fun newsDao(): NewsDao

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
         * 版本升级迁移
         */
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //创建新闻表
                database.execSQL(
                    "CREATE TABLE `news` " +
                            "(uid INTEGER NOT NULL, " +
                            "uniquekey TEXT, " +
                            "title TEXT, " +
                            "date TEXT," +
                            "category TEXT," +
                            "author_name TEXT," +
                            "url TEXT," +
                            "thumbnail_pic_s TEXT," +
                            "is_content TEXT," +
                            "PRIMARY KEY(`uid`))"
                )
                //创建视频表
                database.execSQL(
                    "CREATE TABLE `video` " +
                            "(uid INTEGER NOT NULL, " +
                            "title TEXT," +
                            "share_url TEXT," +
                            "author TEXT," +
                            "item_cover TEXT," +
                            "hot_words TEXT," +
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
                    .addMigrations(MIGRATION_2_3)
                    .build().also { instance = it }
            }
        }
    }
}

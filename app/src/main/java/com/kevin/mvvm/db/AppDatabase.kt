package com.kevin.mvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kevin.mvvm.db.bean.*
import com.kevin.mvvm.db.dao.*


@Database(entities = [Image::class, WallPaper::class, News::class, Video::class, User::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    abstract fun wallPaperDao(): WallPaperDao

    abstract fun videoDao(): VideoDao

    abstract fun newsDao(): NewsDao

    abstract fun userDao(): UserDao

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
         * 版本升级迁移到4 新增用户表
         */
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //创建用户表
                database.execSQL(
                    "CREATE TABLE `user` " +
                            "(uid INTEGER NOT NULL, " +
                            "account TEXT, " +
                            "pwd TEXT, " +
                            "nickname TEXT," +
                            "introduction TEXT," +
                            "PRIMARY KEY(`uid`))"
                )
            }
        }

        /**
         * 版本升级迁移到5 在用户表中新增一个avatar字段
         */
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //User表中新增avatar字段
                database.execSQL("ALTER TABLE `user` ADD COLUMN avatar TEXT")
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
                    .addMigrations(MIGRATION_3_4)
                    .addMigrations(MIGRATION_4_5)
                    .build().also { instance = it }
            }
        }
    }
}

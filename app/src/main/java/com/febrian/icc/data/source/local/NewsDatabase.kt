package com.febrian.icc.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntityNews::class], version = 2, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NewsDatabase {
            if (INSTANCE == null) {
                synchronized(NewsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            NewsDatabase::class.java, "news_database"
                        )
                            .allowMainThreadQueries() //allows Room to executing task in main thread
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE as NewsDatabase
        }
    }
}
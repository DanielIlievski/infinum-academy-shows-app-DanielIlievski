package com.example.infinite_movies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.infinite_movies.converter.UserTypeConverter
import com.example.infinite_movies.database.dao.ReviewDao
import com.example.infinite_movies.database.dao.ShowDao
import com.example.infinite_movies.database.entity.ReviewEntity
import com.example.infinite_movies.database.entity.ShowEntity

@Database(
    entities = [
        ShowEntity::class,
        ReviewEntity::class,
    ],
    version = 1
)
@TypeConverters(UserTypeConverter::class)
abstract class ShowsDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    "shows_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun showDao(): ShowDao

    abstract fun reviewDao(): ReviewDao
}
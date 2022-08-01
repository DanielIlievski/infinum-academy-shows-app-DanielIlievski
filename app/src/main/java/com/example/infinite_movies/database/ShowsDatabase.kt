package com.example.infinite_movies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.infinite_movies.database.dao.ReviewDao
import com.example.infinite_movies.database.dao.ShowDao
import com.example.infinite_movies.database.entity.ReviewEntity
import com.example.infinite_movies.database.entity.ShowEntity

@Database(
    entities = [
        ShowEntity::class
//        ReviewEntity::class
    ],
    version = 1
)
abstract class ShowsDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {

                val database = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    "shows_db"
                )
                    .build()
                INSTANCE = database

                return database

        }
    }

    abstract fun showDao(): ShowDao

//    abstract fun reviewDao(): ReviewDao
}
package com.example.infinite_movies.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.infinite_movies.database.entity.ShowEntity

@Dao
interface ShowDao {

    @Query("SELECT * FROM show")
    fun getAllShows(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM show WHERE id IS :showId")
    fun getShow(showId: Int): LiveData<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<ShowEntity>)

//    @Query("SELECT * FROM show LIMIT 1")
//    fun loadLastShow(): LiveData<ShowEntity>
}
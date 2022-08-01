package com.example.infinite_movies.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.infinite_movies.database.entity.ShowEntity

@Dao
interface ShowDao {

    @Query("SELECT * FROM show")
    fun getAllShows(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM show WHERE id IS :showId")
    fun getShow(showId: String): LiveData<ShowEntity>

    @Insert
    fun insertAllShows(shows: List<ShowEntity>)
}
package com.example.infinite_movies

import android.app.Application
import com.example.infinite_movies.database.ShowsDatabase

class ShowApplication : Application() {

    lateinit var showsDatabase : ShowsDatabase


    override fun onCreate() {
        super.onCreate()

        showsDatabase = ShowsDatabase.getDatabase(applicationContext)
    }
}
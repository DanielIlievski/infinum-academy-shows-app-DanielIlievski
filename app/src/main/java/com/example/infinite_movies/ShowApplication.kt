package com.example.infinite_movies

import android.app.Application
import com.example.infinite_movies.database.ShowsDatabase
import java.util.concurrent.Executors

class ShowApplication : Application() {

    val showsDatabase = ShowsDatabase.getDatabase(this)


    override fun onCreate() {
        super.onCreate()
        // have to invoke in a different Thread
        /* On tuka gi stava superheroes vo DB ama jas gi nemam hardcoded vo Application */
        Executors.newSingleThreadExecutor().execute {
        }
    }
}
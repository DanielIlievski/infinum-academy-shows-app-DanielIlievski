package com.example.infinite_movies.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.database.ShowsDatabase
import com.example.infinite_movies.database.entity.ShowEntity
import com.example.infinite_movies.model.Show
import com.example.infinite_movies.model.ShowsResponse
import com.example.infinite_movies.networking.ApiModule
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val _showsLiveData = MutableLiveData<List<Show>>()
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = getSystemService(context, ConnectivityManager::class.java)
        val activeNetworkInfo = connectivityManager?.activeNetwork
        return activeNetworkInfo != null
    }

    fun hasActiveInternetConnection(context: Context): Boolean {
        if (isNetworkAvailable(context)) {
            try {
                val url = URL("https://www.google.com")
                    .openConnection() as HttpURLConnection
                url.setRequestProperty("User-Agent", "Android")
                url.setRequestProperty("Connection", "close")
                url.connectTimeout = 1500
                url.connect()
                return url.responseCode == 204 &&
                    url.contentLength == 0
            } catch (e: IOException) {
                Log.e(TAG, "Error checking internet connection", e)
            }
        } else {
            Log.d(TAG, "No network available!")
        }
        return false
    }

    fun showListToShowEntityList(showList: List<Show>?): List<ShowEntity>? {
        return showList?.map { show ->
            ShowEntity(show.id, show.avgRating, show.description, show.imgUrl, show.numberOfReviews, show.title)
        }
    }

    fun fetchShowsFromApi() {
        ApiModule.retrofit.fetchShows()
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    if (response.code() == 200) {
                        _showsLiveData.value = response.body()?.shows
                        // add the shows to DB
                        Executors.newSingleThreadExecutor().execute {
                            showListToShowEntityList(response.body()?.shows)?.let { showEntityList ->
                                database.showDao().insertAllShows(showEntityList)
                            }
                        }

                    }
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                }

            })
    }

//    fun isDbEmpty(): LiveData<ShowEntity> {
//        return database.showDao().loadLastShow()
//    }

    fun fetchShowsFromDatabase(): LiveData<List<ShowEntity>> {

        return database.showDao().getAllShows()
    }


}
package com.example.infinite_movies.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.infinite_movies.database.ShowsDatabase
import com.example.infinite_movies.database.entity.ShowEntity
import com.example.infinite_movies.model.Show
import com.example.infinite_movies.model.ShowsResponse
import com.example.infinite_movies.networking.ApiModule
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val _showsLiveData = MutableLiveData<List<Show>>()
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

//    private fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager = getSystemService(context, ConnectivityManager::class.java)
//        val activeNetworkInfo = connectivityManager?.activeNetwork
//        return activeNetworkInfo != null
//    }
//
//    fun hasActiveInternetConnection(context: Context): Boolean {
//        if (isNetworkAvailable(context)) {
//            try {
//                val url = URL("http://clients3.google.com/generate_204")
//                    .openConnection() as HttpURLConnection
//                url.setRequestProperty("User-Agent", "Android")
//                url.setRequestProperty("Connection", "close")
//                url.connectTimeout = 1500
//                url.connect()
//                return url.responseCode == 204 &&
//                    url.contentLength == 0
//            } catch (e: IOException) {
//                Log.e(TAG, "Error checking internet connection", e)
//            }
//        } else {
//            Log.d(TAG, "No network available!")
//        }
//        return false
//    }

    fun showListToShowEntityList(showList: List<Show>?): List<ShowEntity>? {
        return showList?.map { show ->
            ShowEntity(show.id.toString(), show.avgRating, show.description, show.imgUrl, show.numberOfReviews, show.title)
        }
    }

    fun fetchShowsFromApi() {
        ApiModule.retrofit.fetchShows()
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    if (response.code() == 200) {
                        _showsLiveData.value = response.body()?.shows
                        // dodaj gi vo DB
                        showListToShowEntityList(response.body()?.shows)?.let { showEntityList ->
                            database.showDao().insertAllShows(showEntityList)
                        }
                    }
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                }

            })
    }

    fun fetchShowsFromDatabase(): LiveData<List<ShowEntity>> {
        /* Dali vaka da go ostavam ili da smenam showDao().getALlShows() da vrakja List<Show>? */
//        _showsLiveData.value = database.showDao().getAllShows().map { showEntitylist ->
//            for (showEntity in showEntitylist)
//                Show (showEntity.id.toInt(), showEntity.avgRating, showEntity.description, showEntity.imgUrl, showEntity.numberOfReviews, showEntity.title)
//        } as List<Show>

        return database.showDao().getAllShows()
//        showsLiveData = database.showDao().getAllShows()
    }


}
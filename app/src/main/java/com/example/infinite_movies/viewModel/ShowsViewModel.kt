package com.example.infinite_movies.viewModel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.database.ShowsDatabase
import com.example.infinite_movies.database.entity.ShowEntity
import com.example.infinite_movies.errorAlertDialog
import com.example.infinite_movies.model.Show
import com.example.infinite_movies.model.ShowsResponse
import com.example.infinite_movies.model.TopRatedShowsResponse
import com.example.infinite_movies.networking.ApiModule
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel(
    private val context: Context,
    private val database: ShowsDatabase
) : ViewModel() {

    private val _showsLiveData = MutableLiveData<List<Show>>()
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

    private val _progressBarLiveData = MutableLiveData<Int>(View.VISIBLE)
    val progressBarLiveData: LiveData<Int> = _progressBarLiveData

    fun showListToShowEntityList(showList: List<Show>?): List<ShowEntity>? {
        return showList?.map { show ->
            ShowEntity(show.id, show.avgRating, show.description, show.imgUrl, show.numberOfReviews, show.title)
        }
    }

    fun fetchShowsFromApi() {
        ApiModule.retrofit.fetchShows()
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    when (response.code()) {
                        200 -> {
                            _showsLiveData.value = response.body()?.shows
                            _progressBarLiveData.value = View.GONE
                            // add the shows to DB
                            Executors.newSingleThreadExecutor().execute {
                                showListToShowEntityList(response.body()?.shows)?.let { showEntityList ->
                                    database.showDao().insertAllShows(showEntityList)
                                }
                            }
                        }
                        401 -> {
                            errorAlertDialog(context, "You need to sign in or sign up before continuing.")
                        }
                    }
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                }

            })
    }

    fun fetchTopRatedShowsFromApi() {
        ApiModule.retrofit.fetchTopRatedShows()
            .enqueue(object : Callback<TopRatedShowsResponse> {
                override fun onResponse(call: Call<TopRatedShowsResponse>, response: Response<TopRatedShowsResponse>) {
                    when (response.code()) {
                        200 -> {
                            _showsLiveData.value = response.body()?.shows
                            _progressBarLiveData.value = View.GONE
                            // add the shows to DB
                            Executors.newSingleThreadExecutor().execute {
                                showListToShowEntityList(response.body()?.shows)?.let { showEntityList ->
                                    database.showDao().insertAllShows(showEntityList)
                                }
                            }
                        }
                        401 -> {
                            errorAlertDialog(context, "You need to sign in or sign up before continuing.")
                        }
                    }
                }

                override fun onFailure(call: Call<TopRatedShowsResponse>, t: Throwable) {
                }

            })
    }

    fun fetchShowsFromDatabase(): LiveData<List<ShowEntity>> {

        return database.showDao().getAllShows()
    }

    fun fetchTopRatedShowsFromDatabase(): LiveData<List<ShowEntity>> {

        return database.showDao().getTopRatedShows()
    }


}
package com.example.infinite_movies.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.model.Show
import com.example.infinite_movies.model.ShowsResponse
import com.example.infinite_movies.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel() {

    private val _showsLiveData = MutableLiveData<List<Show>>()
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

    private val _progressBarLiveData = MutableLiveData<Int>(View.VISIBLE)
    val progressBarLiveData: LiveData<Int> = _progressBarLiveData

    fun fetchShows() {
        ApiModule.retrofit.fetchShows()
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    if (response.code() == 200) {
                        _showsLiveData.value = response.body()?.shows
                        _progressBarLiveData.value = View.GONE
                    }
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                }

            })
    }


}
package com.example.infinite_movies.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.infinite_movies.database.ShowsDatabase
import com.example.infinite_movies.viewModel.ShowDetailsViewModel
import com.example.infinite_movies.viewModel.ShowsViewModel

class ShowsViewModelFactory(
    val database: ShowsDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(database) as T
        }
        else if(modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(database) as T
        }
        throw IllegalArgumentException("Sorry, we can't work with non ShowsViewModel classes.")
    }
}
package com.example.infinite_movies.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.infinite_movies.database.ShowsDatabase
import com.example.infinite_movies.viewModel.RegisterViewModel
import com.example.infinite_movies.viewModel.ShowDetailsViewModel
import com.example.infinite_movies.viewModel.ShowsViewModel

class ShowsViewModelFactory(
    val context: Context,
    val database: ShowsDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(context, database) as T
        }
        else if(modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(context, database) as T
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(context) as T
        }
        throw IllegalArgumentException("Sorry, we can't work with non ShowsViewModel classes.")
    }
}
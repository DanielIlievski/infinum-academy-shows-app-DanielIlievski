package com.example.infinite_movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.infinite_movies.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
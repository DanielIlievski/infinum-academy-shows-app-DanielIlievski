package com.example.infinite_movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.infinite_movies.databinding.ActivityLoginBinding
import com.example.infinite_movies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}
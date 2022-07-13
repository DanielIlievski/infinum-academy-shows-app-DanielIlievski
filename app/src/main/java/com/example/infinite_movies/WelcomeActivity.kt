package com.example.infinite_movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.infinite_movies.databinding.ActivityLoginBinding
import com.example.infinite_movies.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.welcomeText.text = "Welcome " + intent.extras?.getString("EMAIL")
    }
}
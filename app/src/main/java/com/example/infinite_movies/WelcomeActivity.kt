package com.example.infinite_movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.infinite_movies.databinding.ActivityLoginBinding
import com.example.infinite_movies.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    companion object {
        private const val EXTRA_MAIL = "EXTRA_MAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.welcomeText.text = "Welcome " + intent.extras?.getString(EXTRA_MAIL)
    }
}
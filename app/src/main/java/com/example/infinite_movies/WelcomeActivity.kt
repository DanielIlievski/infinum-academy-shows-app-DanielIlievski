package com.example.infinite_movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.infinite_movies.databinding.ActivityWelcomeBinding
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.welcomeText.text = "Welcome " + intent.extras?.getString("EXTRA_EMAIL")

        // switching automatically to ShowsActivity
        val mHandler = Handler()
        mHandler.postDelayed(object : Runnable {
            override fun run() {
                val intent = ShowsActivity.buildIntent(this@WelcomeActivity)
                intent.putExtra("EXTRA_USERNAME", intent.extras?.getString("EXTRA_EMAIL"))
                startActivity(intent)
            }
        }, 2000)
    }
}
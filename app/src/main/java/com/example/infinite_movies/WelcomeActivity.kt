package com.example.infinite_movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.infinite_movies.databinding.ActivityWelcomeBinding
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.welcomeText.text = String.format(R.string.welcome.toString() + intent.extras?.getString(ShowsActivity.getExtraUsername()))

        // switching automatically to ShowsActivity
        val mHandler = Handler()
        mHandler.postDelayed(object : Runnable {
            override fun run() {
                val username = intent.extras?.getString(ShowsActivity.getExtraUsername()).toString()
                val intent = ShowsActivity.buildIntent(this@WelcomeActivity, username)
                startActivity(intent)
            }
        }, 2000)
    }
}
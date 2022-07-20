package com.example.infinite_movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.infinite_movies.databinding.ActivityWelcomeBinding
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"

        fun buildIntent(activity: Activity, username: String): Intent {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, username)
            return intent
        }
    }

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.welcomeText.text = String.format("Welcome, " + intent.extras?.getString(EXTRA_USERNAME))

        // switching automatically to ShowsActivity
        val mHandler = Handler()
        mHandler.postDelayed(object : Runnable {
            override fun run() {
                val username = intent.extras?.getString(EXTRA_USERNAME).toString()
                val intent = ShowsActivity.buildIntent(this@WelcomeActivity, username)
                startActivity(intent)
            }
        }, 2000)
    }
}
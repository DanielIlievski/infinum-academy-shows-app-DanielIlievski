package com.example.infinite_movies

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private var keepSplashScreenOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplashScreenOn }

        Handler(Looper.getMainLooper())
            .postDelayed({
                keepSplashScreenOn = false
            }, DELAY)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
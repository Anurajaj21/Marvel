package com.example.marvel.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import com.example.marvel.R

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val logo = findViewById<CardView>(R.id.logo)

        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logo.animation = logoAnim
        logo.animation.duration = 1500

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down_anim)
        }, SPLASH_TIME.toLong())
    }
}
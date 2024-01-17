package com.yash.pdfconverter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashScreen : AppCompatActivity() {
    val SPLASH_TIME=800;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

//        this.SPLASH_TIME.rangeTo(10
        Handler().postDelayed(object : Runnable{
            override fun run(){
                var intent= Intent(applicationContext,MainActivity::class.java)

                startActivity(intent)
                finish()
            }
        }, SPLASH_TIME.toLong())

    }
}
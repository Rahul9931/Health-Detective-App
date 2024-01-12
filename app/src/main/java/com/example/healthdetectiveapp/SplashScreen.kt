package com.example.healthdetectiveapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser!=null){
            Handler(Looper.getMainLooper()).postDelayed({
                val next = Intent(this,MainActivity::class.java)
                finish()
                startActivity(next)
            },3000)
        }
        else{
            Handler(Looper.getMainLooper()).postDelayed({
                val next = Intent(this,Login_Activity::class.java)
                finish()
                startActivity(next)
            },3000)
        }

    }
}
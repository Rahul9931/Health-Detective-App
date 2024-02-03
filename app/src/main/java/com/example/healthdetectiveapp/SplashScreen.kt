package com.example.healthdetectiveapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.healthdetectiveapp.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding:ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val bounce = AnimationUtils.loadAnimation(this,R.anim.logo_anim)
        binding.logoImg.startAnimation(bounce)
        val upCome = AnimationUtils.loadAnimation(this,R.anim.come_up)
        binding.hd.startAnimation(upCome)
        val leftCome = AnimationUtils.loadAnimation(this,R.anim.come_left)
        binding.hdFull.startAnimation(leftCome)
        val rightCome = AnimationUtils.loadAnimation(this,R.anim.come_right)
        binding.slogan.startAnimation(rightCome)
        val fade = AnimationUtils.loadAnimation(this,R.anim.fade_anim)
        binding.splashImg.startAnimation(fade)


    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
            Handler(Looper.getMainLooper()).postDelayed({
                val next = Intent(this,MainActivity::class.java)
                finish()
                startActivity(next)
            },4000)
        }
        else{
            Handler(Looper.getMainLooper()).postDelayed({
                val next = Intent(this,Login_Activity::class.java)
                finish()
                startActivity(next)
            },4000)
        }
    }
}
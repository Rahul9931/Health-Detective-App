package com.example.healthdetectiveapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.healthdetectiveapp.databinding.ActivityForgotPasswordBinding
import com.example.healthdetectiveapp.fragment.ForgotPasswordFragment


class ForgotPasswordActivity : AppCompatActivity() {
    private val binding:ActivityForgotPasswordBinding by lazy {
        ActivityForgotPasswordBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadFragment(ForgotPasswordFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.container, fragment)
        ft.commit()
    }
}
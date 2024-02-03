package com.example.healthdetectiveapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthdetectiveapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login_Activity : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var email:String
    private lateinit var passward:String
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initializing
        auth = Firebase.auth

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this,SignUp_Activity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            email = binding.emailLogin.text.toString().trim()
            passward = binding.password.text.toString().trim()

            if (!email.isEmpty()){
                binding.emailLogin.error = null
                if (email.matches("^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())){
                    binding.emailLogin.error = null
                    if (!passward.isEmpty()){
                        binding.password.error = null
                        LoginAccount(email,passward)
                    }
                    else{
                        binding.password.error ="Please Fill Passward"
                    }
                }
                else{
                    binding.emailLogin.error = "Please Fill Valid Email"
                }

            }
            else{
                binding.emailLogin.error ="Please Fill Email"
            }
        }
    }

    private fun LoginAccount(email: String, passward: String) {
        auth.signInWithEmailAndPassword(email,passward).addOnCompleteListener {
                result ->
            if (result.isSuccessful){
                val user = auth.currentUser
                Toast.makeText(this, "Successfully SignIn", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }
            else{
                Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}
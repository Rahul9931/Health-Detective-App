package com.example.healthdetectiveapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthdetectiveapp.databinding.ActivitySignUpBinding
import com.example.healthdetectiveapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.StringBuilder
import java.security.SecureRandom
import java.util.regex.Pattern

class SignUp_Activity : AppCompatActivity() {
    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var username:String
    private lateinit var phone:String
    private lateinit var email:String
    private lateinit var passward:String
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGotoLogin.setOnClickListener {
            startActivity(Intent(this,Login_Activity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            // Geting Values
            username = binding.username.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            email = binding.email.text.toString().trim()
            passward = binding.password.text.toString().trim()

            // Performing Validation
            if (!username.isEmpty()){
                binding.username.error = null
                if (!phone.isEmpty()){
                    binding.phone.error = null
                    if (phone.matches("[1-9][0-9]{9}".toRegex())){
                        binding.phone.error = null
                        if (!email.isEmpty()){
                        binding.email.error = null
                        if (email.matches("^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())){
                            binding.email.error = null
                            if (!passward.isEmpty()){
                            binding.password.error = null
                            createAccount(email,passward)
                        }
                        else{
                            binding.password.error ="Please Fill Passward"
                        }
                        }
                        else{
                            binding.email.error ="Please Fill Valid Email"
                        }
                    }
                    else{
                        binding.email.error ="Please Fill Email"
                    }
                    }
                    else{
                        binding.phone.error = "Please Fill Valid Phone Number"
                    }
                }
                else{
                    binding.phone.error ="Please Fill Phone Number"
                }
            }
            else{
                binding.username.error ="Please Fill Username"
            }

        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                saveUserData()
            }
            else{
                Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        val userId = auth.currentUser!!.uid
        val readId = generatePassword(15)
        if (userId!=null){
            val userRef = database.getReference("UsersProfile").child(userId)
            val userData = UserModel(key = userId, name = username, phone = phone, email = email, passward = passward, readid = readId)
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(this, "Account Created Successful", Toast.LENGTH_SHORT).show()
                val gotoMain = Intent(this,MainActivity::class.java)
                startActivity(gotoMain)
            }

        }
    }

    fun generatePassword(length:Int):String{
        val charSet = ('a'..'z')+('A'..'Z')+(0..9)+ listOf('!','@','#','$','%','^','&','*','?','+','-','/')
        val secureRandom = SecureRandom()
        val password = StringBuilder()
        repeat(length,{
            val charIndex = secureRandom.nextInt(charSet.size)
            password.append(charSet[charIndex])
        })
        return password.toString()
    }
}
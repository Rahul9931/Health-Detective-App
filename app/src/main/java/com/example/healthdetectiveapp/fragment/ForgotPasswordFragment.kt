package com.example.healthdetectiveapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.healthdetectiveapp.Login_Activity
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.FragmentForgotPasswordBinding
import com.example.healthdetectiveapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.regex.Pattern.matches

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding:FragmentForgotPasswordBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private var registerFlag=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        // Radio Button Switching
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioButton_email->{
                    binding.mobContainer.visibility = View.GONE
                    binding.emailContainer.visibility = View.VISIBLE
                }
                R.id.radioButton_mobno->{
                    binding.emailContainer.visibility = View.GONE
                    binding.mobContainer.visibility = View.VISIBLE
                }
            }
        }
        binding.btnGetotp.setOnClickListener {
            val number = binding.number.text.toString().trim()
            if (number.isNotEmpty()){
                if (number.matches("[1-9][0-9]{9}".toRegex())){
                    isNumberRegister(number)
                } else{
                    binding.number.error = "Please Enter Valid Number"
                }
            } else{
                binding.number.error = "Please Enter Number"
            }
        }
        binding.btnSubmit.setOnClickListener {
            val email = binding.email.text.toString().trim()
            if (email.isNotEmpty()){
                binding.email.error = null
                if (email.matches("^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())){
                    forgotPassword(email)
                }
                else{
                    binding.email.error = "Please Fill Valid Email"
                }
            }
            else{
                binding.email.error = "Please Fill Email Id"
            }
        }

        return binding.root
    }

    private fun isNumberRegister(number: String) {
        database.reference.child("UsersProfile").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val numbersList= mutableListOf<String>()
                for (usersData in snapshot.children){
                    val eachNumber = usersData.child("phone").getValue().toString()
                    numbersList.add(eachNumber)
                }
                Log.d("check_numbers1","$numbersList")
                Log.d("check_numbers2","$number")
              val list = numbersList.filter {
                  it.equals(number)
              }
                if (list.isEmpty()){
                    Toast.makeText(requireContext(), "This number is not registered", Toast.LENGTH_LONG).show()
                }else{
                    loadFragment(OTPVerificationFragment(),number)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun loadFragment(fragment: Fragment, number: String) {
        val bundle = Bundle()
        val fm = parentFragmentManager
        val ft = fm.beginTransaction()
        bundle.putString("number",number)
        fragment.arguments = bundle
        ft.replace(R.id.container,fragment)
        ft.commit()
    }

    private fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            Toast.makeText(requireContext(), "Check Your Email", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(),Login_Activity::class.java))
        }
            .addOnFailureListener {
                Log.d("fp_status","${it.message}")
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
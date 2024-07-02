package com.example.healthdetectiveapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.healthdetectiveapp.MainActivity
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.FragmentOTPVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class OTPVerificationFragment : Fragment() {
    private lateinit var binding:FragmentOTPVerificationBinding
    private val auth = FirebaseAuth.getInstance()
    private lateinit var dialog: AlertDialog
    private lateinit var firebaseOTP:String
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
        binding = FragmentOTPVerificationBinding.inflate(inflater,container,false)
        val number = "+91" + arguments?.getString("number")
        setDialog()
        dialog.show()
        generateOTP(number)
        numberMover()
        binding.btnContinue.setOnClickListener {
            validateInput()
        }
        // Resend OTP
        binding.btnResend.setOnClickListener {
            generateOTP(number)
        }
        return binding.root
    }

    private fun validateInput() {
        val n1 = binding.edt1.text.toString()
        val n2 = binding.edt2.text.toString()
        val n3 = binding.edt3.text.toString()
        val n4 = binding.edt4.text.toString()
        val n5 = binding.edt5.text.toString()
        val n6 = binding.edt6.text.toString()
        if (n1.isEmpty() || n2.isEmpty() || n3.isEmpty() || n4.isEmpty() || n5.isEmpty() || n6.isEmpty()){
            Toast.makeText(requireContext(), "Please fill all numbers", Toast.LENGTH_LONG).show()
        }
        else{
            val userOTP = n1 + n2 + n3 + n4 + n5 + n6
            Log.d("check_numbers","firebaseOTP = $firebaseOTP and userOTP = $userOTP")
            val credential = PhoneAuthProvider.getCredential(firebaseOTP,userOTP)
            auth.signInWithCredential(credential)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(), "Successfull SignIn", Toast.LENGTH_LONG).show()
                        startActivity(Intent(requireContext(),MainActivity::class.java))
                        activity?.finish()
                    }
                    else{
                        Log.d("check_otp_error2","${it.exception}")
                        Toast.makeText(requireContext(), "error 2 = ${it.exception}", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    private fun generateOTP(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    dialog.dismiss()
                    Log.d("check_otp_error1","$e")
                    Toast.makeText(requireContext(), "error 1 = ${e}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    firebaseOTP = p0
                }

            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        /*binding.btnContinue.setOnClickListener {
            if (binding.edtOtp.text.isEmpty()){
                Toast.makeText(requireContext(), "Text Field Empty", Toast.LENGTH_SHORT).show()
            }
            else{
                val credential = PhoneAuthProvider.getCredential(firebaseOTP,binding.edtOtp.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(requireContext(), "Successfull SignIn", Toast.LENGTH_LONG).show()
                            startActivity(Intent(requireContext(),MainActivity::class.java))
                            activity?.finish()
                        }
                        else{
                            Log.d("otp_error2","${it.exception}")
                            Toast.makeText(requireContext(), "error 2 = ${it.exception}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }*/

    }

    private fun setDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Loading")
        alertDialog.setMessage("Please Wait")
        alertDialog.setCancelable(false)
        dialog = alertDialog.create()
    }

    private fun numberMover() {
        binding.edt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()){
                    binding.edt2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()){
                    binding.edt3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edt3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()){
                    binding.edt4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edt4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()){
                    binding.edt5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edt5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()){
                    binding.edt6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }
}
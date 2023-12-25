package com.example.healthdetectiveapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.healthdetectiveapp.databinding.ActivityPatientInformationBinding

class Patient_Information_Activity : AppCompatActivity() {
    private val binding:ActivityPatientInformationBinding by lazy {
        ActivityPatientInformationBinding.inflate(layoutInflater)
    }
    private var patientName:String?=null
    private var patientAge:String?=null
    private var patientGender:String?=null
    private var patientDieases:String?=null
    private var patientAddress:String?=null
    private var patientPhone:String?=null
    private var patientEmail:String?=null
    private var patientDate:String?=null
    private var patientTime:String?=null
    private var patientImage:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initalizing variables
        patientName = intent.getStringExtra("patientName")
        patientAge = intent.getStringExtra("patientAge")
        patientGender = intent.getStringExtra("patientGender")
        patientDieases = intent.getStringExtra("patientDieases")
        patientAddress = intent.getStringExtra("patientAddress")
        patientPhone = intent.getStringExtra("patientPhone")
        patientEmail = intent.getStringExtra("patientEmail")
        patientDate = intent.getStringExtra("patientDate")
        patientTime = intent.getStringExtra("patientTime")
        patientImage = intent.getStringExtra("patientImage")

        // Set Data
        binding.apply {
            patnameEdt.setText(patientName)
            patageEdt.setText(patientAge)
            patgenderEdt.setText(patientGender)
            patdieasesEdt.setText(patientDieases)
            addedt.setText(patientAddress)
            phoneedt.setText(patientPhone)
            emailedt.setText(patientEmail)
            dateedt.setText(patientDate)
            timeedt.setText(patientTime)
            Glide.with(this@Patient_Information_Activity).load(Uri.parse(patientImage)).into(patientImagePatinfo)
        }
    }
}
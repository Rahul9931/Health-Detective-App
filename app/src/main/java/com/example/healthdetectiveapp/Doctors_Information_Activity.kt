package com.example.healthdetectiveapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.healthdetectiveapp.databinding.ActivityDoctorsInformationBinding

class Doctors_Information_Activity : AppCompatActivity() {
    private val binding:ActivityDoctorsInformationBinding by lazy {
        ActivityDoctorsInformationBinding.inflate(layoutInflater)
    }
    private var doctorsname:String? = null
    private var specilist:String? = null
    private var rating:String? = null
    private var doctorimages:String? = null
    private var noOfPatients:String? = null
    private var experience:String? = null
    private var aboutus:String? = null
    private var workingat:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initializing variables
        doctorsname = intent.getStringExtra("doctorname")
        specilist = intent.getStringExtra("speciality")
        noOfPatients = intent.getStringExtra("noofPatients")
        experience = intent.getStringExtra("experience")
        rating = intent.getStringExtra("rating")
        aboutus = intent.getStringExtra("aboutus")
        workingat = intent.getStringExtra("workingat")
        doctorimages = intent.getStringExtra("doctorimage")

        // Set data
        binding.dname.text = doctorsname
        binding.specialistAppointment.text = specilist
        binding.noofrating.text = rating
        binding.noofpatients.text = noOfPatients
        binding.noofexp.text = experience
        binding.aboutus.text = aboutus
        binding.workingat.text = workingat
        Glide.with(this).load(Uri.parse(doctorimages)).into(binding.doctorimageAppointment)

        // Set Appointment
        binding.btnGotoAppointment.setOnClickListener {
            val gotoappointmentIntent = Intent(this,Appointment_Activity::class.java)
            startActivity(gotoappointmentIntent)
        }

        // Dial Number
        binding.phone.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.setData(Uri.parse("tel: +918810566785"))
            startActivity(Intent.createChooser(dialIntent,"Call Via"))
        }

        // Send Messages
        binding.chat.setOnClickListener {
            val chatIntent = Intent(Intent.ACTION_SENDTO)
            chatIntent.setData(Uri.parse("smsto:"+Uri.encode("+918810566785")))
            startActivity(Intent.createChooser(chatIntent,"Chat Via"))
        }
    }
}
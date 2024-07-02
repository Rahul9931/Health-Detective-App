package com.example.healthdetectiveapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.healthdetectiveapp.databinding.ActivityBmiResultBinding

class BMI_Result_Activity : AppCompatActivity() {
    private val binding: ActivityBmiResultBinding by lazy {
        ActivityBmiResultBinding.inflate(layoutInflater)
    }
    private var index:Int?=null
    private lateinit var dieases:String
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var bmiResult = intent.getDoubleExtra("bmiresult",3.0)
        var height = intent.getDoubleExtra("height",4.0)
        var weight = intent.getDoubleExtra("weight",5.0)
        binding.bmivalue.text = bmiResult.toString()
        binding.heightvalue.text = height.toString()
        binding.weightvalue.text = weight.toString()

        if (bmiResult<18.5){
            binding.cardcontainer.background.setTint(resources.getColor(R.color.skyblue9))
            binding.msg.text = "You Are Underweight"
            binding.msg.setTextColor(resources.getColor(R.color.skyblue9))
            dieases = "Underweight - अवजान"
            index = 44
        }
        else if (bmiResult>=18.5 && bmiResult<25){
            binding.cardcontainergreen.background.setTint(resources.getColor(R.color.green))
            binding.msg.text = "You Have Normal Body \nWeight"
            binding.msg.setTextColor(resources.getColor(R.color.green))
            binding.resultContainer.visibility = View.GONE
        }
        else if (bmiResult>25 && bmiResult<30){
            binding.cardcontainerorange.background.setTint(resources.getColor(R.color.orange))
            binding.msg.text = "You Are Overweight"
            binding.msg.setTextColor(resources.getColor(R.color.orange))
            dieases = "Overweight - अतिरिक्त वजन"
            index = 45
        }
        else{
            binding.cardcontainerred.background.setTint(resources.getColor(R.color.red))
            binding.msg.text = "You Have Obese Body \nWeight"
            binding.msg.setTextColor(resources.getColor(R.color.red))
            dieases = "Obese - मोटापा"
            index = 46
        }
        // Set BMI Category Result information
        binding.bmiresultInfo.setOnClickListener {
            val bmiResultIntent = Intent(this, DieasesInformation_Activity::class.java)
            bmiResultIntent.putExtra("requestCode","dieasesInformation")
            bmiResultIntent.putExtra("urlIndex",index)
            startActivity(bmiResultIntent)
        }

        // Set Doctor Appointment
        binding.doctorAppointmentBmi.setOnClickListener {
            val appointmentIntent = Intent(this, MainActivity::class.java)
            appointmentIntent.putExtra("OpenScreen","doctorAppointment")
            appointmentIntent.putExtra("dieases",dieases)
            startActivity(appointmentIntent)
        }


    }
}
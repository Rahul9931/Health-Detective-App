package com.example.healthdetectiveapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.healthdetectiveapp.BMI_Result_Activity
import com.example.healthdetectiveapp.DieasesInformation_Activity
import com.example.healthdetectiveapp.databinding.FragmentBmiCalculatorBinding

class BMICalculatorFragment : Fragment() {
    private lateinit var binding: FragmentBmiCalculatorBinding
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
        binding = FragmentBmiCalculatorBinding.inflate(layoutInflater)

        binding.feetpicker.minValue = 3
        binding.feetpicker.maxValue = 8

        binding.inchpicker.minValue = 1
        binding.inchpicker.maxValue = 12

        binding.weightpicker2.minValue = 25
        binding.weightpicker2.maxValue = 250

        binding.calculatebmi.setOnClickListener {
            var ft = binding.feetpicker.value.toDouble()
            var inc = binding.inchpicker.value.toDouble()
            var totalinc = ft*12 + inc
            var totalcm = totalinc*2.54
            var totalm = totalcm/100
            var weight = binding.weightpicker2.value.toDouble()
            var bmi = weight/(totalm*totalm)
            totalcm = String.format("%.1f",totalcm).toDouble()
            bmi = String.format("%.1f",bmi).toDouble()
            Toast.makeText(requireContext(),"$bmi", Toast.LENGTH_LONG).show()
            val bmiResultIntent = Intent(requireContext(),BMI_Result_Activity::class.java)
            bmiResultIntent.putExtra("bmiresult",bmi)
            bmiResultIntent.putExtra("height",totalcm)
            bmiResultIntent.putExtra("weight",weight)
            startActivity(bmiResultIntent)
        }

        binding.whatbmi.setOnClickListener {
            val whatbmiIntent = Intent(requireContext(), DieasesInformation_Activity::class.java)
            whatbmiIntent.putExtra("urlIndex",47)
            whatbmiIntent.putExtra("requestCode","bmiResultInformation")
            startActivity(whatbmiIntent)
        }
        return binding.root
    }
}
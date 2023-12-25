package com.example.healthdetectiveapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthdetectiveapp.DieasesDiagnoseByImage
import com.example.healthdetectiveapp.Dieases_Detection_Activity
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        binding.dieasesonsymptoms.setOnClickListener {
            startActivity(Intent(requireContext(),Dieases_Detection_Activity::class.java))
        }

        binding.brainTumarCard.setOnClickListener {
            val brainIntent = Intent(requireContext(),DieasesDiagnoseByImage::class.java)
            brainIntent.putExtra("cardKey",1)
            startActivity(brainIntent)
        }
        binding.AlzimerCard.setOnClickListener {
            val alzimerIntent = Intent(requireContext(),DieasesDiagnoseByImage::class.java)
            alzimerIntent.putExtra("cardKey",2)
            startActivity(alzimerIntent)
        }
        binding.LungsCancerCard.setOnClickListener {
            val lungsCancerIntent = Intent(requireContext(),DieasesDiagnoseByImage::class.java)
            lungsCancerIntent.putExtra("cardKey",3)
            startActivity(lungsCancerIntent)
        }
        return binding.root
    }

}
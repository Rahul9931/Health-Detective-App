package com.example.healthdetectiveapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.healthdetectiveapp.DieasesInformation_Activity
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.ActivityDieasesInformationBinding
import com.example.healthdetectiveapp.databinding.FragmentDieasesInformationBinding

class DieasesInformationFragment : Fragment() {
    private lateinit var binding: FragmentDieasesInformationBinding
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
        binding = FragmentDieasesInformationBinding.inflate(layoutInflater,container,false)
        // Fetching dieases from dieases.txt
        val fileName = "disease_HE"
        val inputString = context?.assets?.open(fileName)?.bufferedReader().use { it?.readText() }
        var dieasesList = inputString?.split("\n")

        // Set AutocompleteTextView
        val autocompleteAdapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,dieasesList as MutableList<String>)
        val autoComplete = binding.autoCompleteDieasesinfofr
        autoComplete.setAdapter(autocompleteAdapter)

        // Set Get Information Button
        binding.getdieasesinfo.setOnClickListener {
            val dieases = autoComplete.text.toString()
            Log.d("check_disease","$dieases")
            if (dieases.isNotEmpty()){
                val index = dieasesList.indexOf(dieases)
                Log.d("check_index1","$index")
                val dieasesInfoIntent = Intent(requireContext(), DieasesInformation_Activity::class.java)
                dieasesInfoIntent.putExtra("urlIndex",index)
                dieasesInfoIntent.putExtra("requestCode","dieasesInformation")
                startActivity(dieasesInfoIntent)
            }
            else{
                Toast.makeText(requireContext(), "Please Select Dieases", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}
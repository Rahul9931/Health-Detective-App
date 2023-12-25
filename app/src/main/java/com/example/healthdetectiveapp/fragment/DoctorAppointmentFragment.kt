package com.example.healthdetectiveapp.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.Recommender_Activity
import com.example.healthdetectiveapp.adapter.DoctorsListAdapter
import com.example.healthdetectiveapp.databinding.FragmentDoctorAppointmentBinding
import com.example.healthdetectiveapp.model.doctors
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DoctorAppointmentFragment : Fragment() {
    private lateinit var binding:FragmentDoctorAppointmentBinding
    private var doctorsArrayList : ArrayList<doctors> = ArrayList()
    private lateinit var database: FirebaseDatabase
    private lateinit var dialog: Dialog
    private lateinit var finaldieases:MutableList<String>
    private lateinit var specialistList: List<String>
    private lateinit var setfilter: Button
    private lateinit var dieases: AutoCompleteTextView
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
        binding = FragmentDoctorAppointmentBinding.inflate(layoutInflater,container,false)

        // Fetching dieases from dieaseslabels.txt
        val fileName = "alldieases.txt"
        val inputString = context?.assets?.open(fileName)?.bufferedReader().use { it?.readText() }
        finaldieases = inputString?.split("\n") as MutableList<String>

        val specialist = "allspecialistforfilter.txt"
        val specialistInput = context?.assets?.open(specialist)?.bufferedReader().use { it?.readText() }
        specialistList = specialistInput!!.split("\n")

        // Fetching Doctors List
        fetchDoctorsList()
        Log.d("argument_doctorsList","$doctorsArrayList")

        // Creating Filter Dialog
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.filter_dialog)
        setfilter = dialog.findViewById(R.id.btn_setfilter)
        dieases = dialog.findViewById(R.id.autoComplete_filter)

        // Show Filter Dialog on the DoctorsList
        binding.filter.setOnClickListener {
            // Set AutocompleteTextView
            val autocompleteAdapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,finaldieases)
            dieases.setAdapter(autocompleteAdapter)
            dialog.show()
        }

        // Set Filter Button
        setfilter.setOnClickListener {
            val dieases_text = dieases.text.toString().trim()
            if (dieases_text.isNotEmpty()){
                Log.d("check_dieasestext","$dieases_text")
                val specialist = dieasesToSpecialist(dieases_text)
                Log.d("check_specialist","$specialist")
                val list = filterSpecialist(specialist)
                Log.d("check_list","$list")
                setAdapter(list as ArrayList<doctors>)
                dieases.text.clear()
                dialog.dismiss()
            }
            else{
                dialog.dismiss()
            }

        }
        return binding.root
    }

    private fun dieasesToSpecialist(dieasesLabel:String):String {
        var ind= 0
        Log.d("dieasesToSpecialist5","${finaldieases[2]}")
        Log.d("dieasesToSpecialist6","${finaldieases[2].length}")
        for (i in 0..finaldieases.size-1){
            if (finaldieases[i].trim().equals(dieasesLabel.trim())){
                ind = i
                Log.d("dieasesToSpecialist1","${i}")
            }
            Log.d("dieasesToSpecialist2","$i")
        }
        Log.d("dieasesToSpecialist3","${finaldieases[ind]}")
        Log.d("dieasesToSpecialist4","${dieasesLabel}")
        val specialist = specialistList.get(ind)
        return specialist
    }

    fun filterSpecialist(specialist:String):List<doctors> {
            val list = doctorsArrayList.filter {
                specialist.trim().contains(it.specilist.toString().trim())
            }
            return list
    }

    private fun fetchDoctorsList() {
        database = FirebaseDatabase.getInstance()
        val doctorListRef: DatabaseReference = database.reference.child("DoctorsList")
        doctorListRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                doctorsArrayList.clear()
                for (doctorsSnapshot in snapshot.children) {
                    val eachItem = doctorsSnapshot.getValue(doctors::class.java)
                    eachItem?.let { result ->
                        doctorsArrayList.add(result)
                    }
                }
                // Check Bundle Passing
                val dieasesLabel = requireArguments().get("dieases").toString()
                if (dieasesLabel.equals("null")){
                    Log.d("argument_if", "$arguments")
                    setAdapter(doctorsArrayList)
                }
                else{
                    Log.d("argument_else", "$arguments")
                    val specialist = dieasesToSpecialist(dieasesLabel)
                    val list = filterSpecialist(specialist)
                    Log.d("argument_dL_length","${dieasesLabel.length}")
                    Log.d("argument_dieasesLabel", "$dieasesLabel")
                    Log.d("argument_specialist", "$specialist")
                    Log.d("argument_list", "$list")
                    setAdapter(list as ArrayList<doctors>)
                }
                Log.d("doctorList","${doctorsArrayList}")
                Toast.makeText(requireContext(), "Process Success", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Process Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter(docArrayList: ArrayList<doctors>) {
        binding.rvdoctorlist.layoutManager = LinearLayoutManager(requireContext())
        val doctorListAdapter = DoctorsListAdapter(requireContext(), docArrayList)
        binding.rvdoctorlist.adapter = doctorListAdapter
    }

}
package com.example.healthdetectiveapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.adapter.AppointmentStatusAdapter
import com.example.healthdetectiveapp.databinding.FragmentAppointmentStatusBinding
import com.example.healthdetectiveapp.model.PatientModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Appointment_Status_Fragment : Fragment() {
    private lateinit var binding:FragmentAppointmentStatusBinding
    private lateinit var database: FirebaseDatabase
    private var patientArrayList:ArrayList<PatientModel> = ArrayList()
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
        binding = FragmentAppointmentStatusBinding.inflate(layoutInflater,container,false)
        fetchPatientData()
        return binding.root
    }

    private fun fetchPatientData() {
        database = FirebaseDatabase.getInstance()
        val patientListRef:DatabaseReference = database.reference.child("PatientsList")
        patientListRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                patientArrayList.clear()
                for (patientSnapshot in snapshot.children){
                    var eachValue = patientSnapshot.getValue(PatientModel::class.java)
                    eachValue?.let {
                        patientArrayList.add(it)
                    }
                }
                setAdapter(patientArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapter(patientsList: ArrayList<PatientModel>) {
        binding.rvAppointmentStatus.layoutManager = LinearLayoutManager(requireContext())
        val appStatusAdapter = AppointmentStatusAdapter(requireContext(),patientsList)
        binding.rvAppointmentStatus.adapter = appStatusAdapter
    }
}
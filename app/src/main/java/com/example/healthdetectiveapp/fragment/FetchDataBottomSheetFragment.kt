package com.example.healthdetectiveapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.adapter.PatientsRecordsAdapter
import com.example.healthdetectiveapp.databinding.FragmentFetchDataBottomSheetBinding
import com.example.healthdetectiveapp.model.FileInformationModel
import com.example.healthdetectiveapp.model.PatientsRecordsModel
import com.example.healthdetectiveapp.model.UserModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File

class FetchDataBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFetchDataBottomSheetBinding
    private var recyclerView:RecyclerView?=null
    private var database = FirebaseDatabase.getInstance()
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
        binding = FragmentFetchDataBottomSheetBinding.inflate(layoutInflater)
        binding.btnGetrecords.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val readid = binding.edtReadid.text.toString().trim()
            validate(email,readid)
        }

        binding.btndismiss.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    private fun validate(email: String, readid: String) {
        if (!email.isEmpty()){
            binding.edtEmail.error = null
            if (!readid.isEmpty()){
                binding.edtReadid.error = null
                authenticate(email,readid)
            }
            else{
                binding.edtReadid.error = "Please Fill ReadId"
            }
        }
        else{
            binding.edtEmail.error = "Please Fill EmailId"
        }
    }

    private fun authenticate(email: String, readid: String) {
        val patientsRef = database.reference.child("UsersProfile")
        patientsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val patientsList = mutableListOf<UserModel>()
                for (dataSnapshot in snapshot.children){
                    val each = dataSnapshot.getValue(UserModel::class.java)
                    each?.let { patientsList.add(it) }
                }
                Log.d("patientRef_record","${patientsList}")
                patientsList.map {
                    if (it.email?.trim().equals(email)){
                        binding.edtEmail.error = null
                        if (it.readid?.trim().equals(readid)){
                            binding.edtReadid.error = null
                            val patientKey = it.key
                            getPatientRecord(patientKey)
                        }
                        else{
                            binding.edtReadid.error = "Read Id Not Match"
                        }
                    }
                    else{
                        binding.edtEmail.error = "Email Id Not Match"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getPatientRecord(patientKey: String?) {
        val patientRecordsRef = database.reference.child("PatientsRecords").child("${patientKey}")
        patientRecordsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dateWiseFilesList = mutableListOf<PatientsRecordsModel>()
                for (dateSnapshot in snapshot.children){
                    val date = dateSnapshot.key.toString()
                    val filesList = mutableListOf<FileInformationModel>()
                    for (filesSnapshot in dateSnapshot.children){
                        val eachFile = filesSnapshot.getValue(FileInformationModel::class.java)
                        eachFile?.let { filesList.add(it) }
                    }
                    val data = PatientsRecordsModel(date,filesList)
                    dateWiseFilesList.add(data)
                }
                setAdapter(dateWiseFilesList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapter(dateWiseFilesList: MutableList<PatientsRecordsModel>) {
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        val patientsRecordsAdapter = PatientsRecordsAdapter(requireContext(),dateWiseFilesList)
        recyclerView?.adapter = patientsRecordsAdapter
        dismiss()
    }

    fun setRecyclerView(rvPatientsrecord: RecyclerView) {
        this.recyclerView = rvPatientsrecord
    }

}
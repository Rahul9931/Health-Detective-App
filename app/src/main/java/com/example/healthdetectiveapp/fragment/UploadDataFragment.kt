package com.example.healthdetectiveapp.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.adapter.UploadFilesAdapter
import com.example.healthdetectiveapp.databinding.FragmentUploadDataBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.healthdetectiveapp.adapter.PatientsRecordsAdapter
import com.example.healthdetectiveapp.model.FileInformationModel
import com.example.healthdetectiveapp.model.PatientsRecordsModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UploadDataFragment : Fragment() {
    private lateinit var binding: FragmentUploadDataBinding
    private val PICK_FILE_REQUEST = 101
    private lateinit var files:ArrayList<String>
    private lateinit var status:ArrayList<String>
    private lateinit var uploadFileAdapter:UploadFilesAdapter
    private var fileName:String?=null
    private lateinit var imageUri:Uri
    val storageReference = FirebaseStorage.getInstance().reference
    val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadDataBinding.inflate(inflater, container, false)
        // Fetch Patients Records from Firebase
        fetchPatientsRecords()
        // Shrink Floating Button
        binding.rvPatientsrecord.shrinkFabOnScroll(binding.btnFloat)

        // Show uploadRecordFragment after clicking Floating Button
        binding.btnFloat.setOnClickListener{
            val patientsRecordBS = uploadRecordFragment()
            patientsRecordBS.show(parentFragmentManager,"RecordDialog")
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.fetch_data -> {
                val fetchDataBottomSheetFragment = FetchDataBottomSheetFragment()
                fetchDataBottomSheetFragment.setRecyclerView(binding.rvPatientsrecord)
                fetchDataBottomSheetFragment.show(parentFragmentManager,"fetchdata")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchPatientsRecords() {
        val userid = auth.currentUser!!.uid
        val patientsRecordsRef = database.reference.child("PatientsRecords")
        val userIdRef = patientsRecordsRef.child(userid)
        userIdRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dateFilesList:MutableList<PatientsRecordsModel> = mutableListOf()
                for (dateSnapshot in snapshot.children){
                    Log.d("exam_dateSnapshot","${dateSnapshot}")
                    val date = dateSnapshot.key.toString()
                    Log.d("exam_date","${date}")
                    val filesList:MutableList<FileInformationModel> = mutableListOf()
                    for (fileSnapshot in dateSnapshot.children){
                        Log.d("exam_fileSnapshot","${fileSnapshot}")
                        val eachFile = fileSnapshot.getValue(FileInformationModel::class.java)
                        Log.d("exam_eachFile","${eachFile}")
                        eachFile?.let { filesList.add(it) }
                    }
                    val dateFiles = PatientsRecordsModel(date,filesList)
                    dateFilesList.add(dateFiles)
                }
                setAdapter(dateFilesList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapter(dateFilesList: MutableList<PatientsRecordsModel>) {
        binding.rvPatientsrecord.layoutManager = LinearLayoutManager(requireContext())
        val patientsRecordAdapter = PatientsRecordsAdapter(requireContext(),dateFilesList)
        binding.rvPatientsrecord.adapter = patientsRecordAdapter
    }

    private fun customStatus(s1:String){
        Toast.makeText(requireContext(), "${s1}", Toast.LENGTH_SHORT).show()
    }
}
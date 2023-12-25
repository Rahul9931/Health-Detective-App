package com.example.healthdetectiveapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthdetectiveapp.adapter.DoctorsListAdapter
import com.example.healthdetectiveapp.databinding.ActivityRecommenderBinding
import com.example.healthdetectiveapp.model.doctors
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Recommender_Activity : AppCompatActivity() {
    private val binding:ActivityRecommenderBinding by lazy{
        ActivityRecommenderBinding.inflate(layoutInflater)
    }
    private var doctorsArrayList : ArrayList<doctors> = ArrayList()
    private lateinit var database: FirebaseDatabase
    private lateinit var dialog:Dialog
    private lateinit var finaldieases:MutableList<String>
    private lateinit var specialistList: List<String>
    private lateinit var setfilter:Button
    private lateinit var dieases:AutoCompleteTextView
    private var ind:Int = 0
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchDoctorsList()

        // Fetching dieases from dieaseslabels.txt
        val fileName = "alldieases.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        finaldieases = inputString.split("\n") as MutableList<String>

        val specialist = "allspecialistforfilter.txt"
        val specialistInput = application.assets?.open(specialist)?.bufferedReader().use { it?.readText() }
        specialistList = specialistInput!!.split("\n")

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.filter_dialog)
        setfilter = dialog.findViewById(R.id.btn_setfilter)
        dieases = dialog.findViewById(R.id.autoComplete_filter)

        // Set Filter on the DoctorsList
        binding.filter.setOnClickListener {
            // Set AutocompleteTextView
            val autocompleteAdapter = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,finaldieases)
            dieases.setAdapter(autocompleteAdapter)
            filterSpecialist()
        }
    }
    fun fetchDoctorsList() {
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
                setAdapter(doctorsArrayList)
                Log.d("doctorList","${doctorsArrayList}")
                Toast.makeText(this@Recommender_Activity, "Process Success", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Recommender_Activity, "Process Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter(docArrayList: ArrayList<doctors>) {
        binding.rvdoctorlist.layoutManager = LinearLayoutManager(this)
        val doctorListAdapter = DoctorsListAdapter(this, docArrayList)
        binding.rvdoctorlist.adapter = doctorListAdapter
    }

    fun filterSpecialist() {
        dialog.show()
        setfilter.setOnClickListener {
            val dieases_text = dieases.text.toString().trim()
            Log.d("dieases_text",dieases_text)
            for (i in 0..finaldieases.size-1){
                if (finaldieases[i].trim().equals(dieases_text)){
                    ind = i
                }

            }
                Log.d("dieasesIndex","${ind}")
                Log.d("finaldieaseslength","${finaldieases.size}")
                Log.d("finaldieases","${finaldieases}")
                Log.d("specialistlength","${specialistList.size}")
                Log.d("specialist2","${specialistList}")
                val specialist = specialistList.get(ind)
                Log.d("specialist","${specialist}")
                val list = doctorsArrayList.filter {
                    it.specilist.toString().trim().contains("${specialist.trim()}")
                }
                Log.d("specialistList","${list}")
                setAdapter(list as ArrayList<doctors>)
            dieases.text.clear()
            dialog.dismiss()
        }
        Log.d("specialist","${doctorsArrayList}")


    }
}
package com.example.healthdetectiveapp

import android.content.Context
import android.widget.Toast
import com.example.healthdetectiveapp.model.doctors
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

open class FBDatabase(
    private var context: Context
) {
        private lateinit var database: FirebaseDatabase
        private var doctorsArrayList: ArrayList<doctors> = ArrayList()


        // Fetching Doctors List
        fun fetchDoctorsList(): ArrayList<doctors> {
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
                    Toast.makeText(context, "Process Success", Toast.LENGTH_SHORT).show()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show()
                }

            })
            return doctorsArrayList
        }


}
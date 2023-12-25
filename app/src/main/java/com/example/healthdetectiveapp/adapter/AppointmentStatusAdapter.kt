package com.example.healthdetectiveapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthdetectiveapp.Patient_Information_Activity
import com.example.healthdetectiveapp.databinding.DoctorsCardBinding
import com.example.healthdetectiveapp.databinding.PatientCardBinding
import com.example.healthdetectiveapp.model.PatientModel
import com.example.healthdetectiveapp.model.doctors

class AppointmentStatusAdapter(
    private var context: Context,
    private val patientList: ArrayList<PatientModel>
):
    RecyclerView.Adapter<AppointmentStatusAdapter.AppointmentStatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentStatusViewHolder {
        val binding = PatientCardBinding.inflate(LayoutInflater.from(context),parent,false)
        return AppointmentStatusViewHolder(binding)
    }

    override fun getItemCount(): Int = patientList.size

    override fun onBindViewHolder(holder: AppointmentStatusViewHolder, position: Int) {
        val patientListItems = patientList[position]
         holder.binding.apply {
             patientname.text = patientListItems.patientName
             dieases.text = patientListItems.dieases
             dateAppstatus.text = patientListItems.date
             timeAppstatus.text = patientListItems.time
             Glide.with(context).load(Uri.parse(patientListItems.patientImage)).into(patientimage)
             // Set ClickOnListener On Card
             patientCard.setOnClickListener {
                 val appStatusIntent = Intent(context,Patient_Information_Activity::class.java)
                 appStatusIntent.apply {
                     putExtra("patientName",patientListItems.patientName)
                     putExtra("patientAge",patientListItems.age)
                     putExtra("patientGender",patientListItems.gender)
                     putExtra("patientDieases",patientListItems.dieases)
                     putExtra("patientAddress",patientListItems.address)
                     putExtra("patientPhone",patientListItems.phoneno)
                     putExtra("patientEmail",patientListItems.email)
                     putExtra("patientDate",patientListItems.date)
                     putExtra("patientTime",patientListItems.time)
                     putExtra("patientImage",patientListItems.patientImage)
                 }
                 context.startActivity(appStatusIntent)
             }
         }
    }

    class AppointmentStatusViewHolder(val binding: PatientCardBinding):RecyclerView.ViewHolder(binding.root) {

    }
}
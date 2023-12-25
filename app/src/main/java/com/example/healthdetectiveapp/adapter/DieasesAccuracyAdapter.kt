package com.example.healthdetectiveapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthdetectiveapp.DieasesInformation_Activity
import com.example.healthdetectiveapp.MainActivity
import com.example.healthdetectiveapp.databinding.DieasesAccuracyCardBinding

class DieasesAccuracyAdapter(val context:Context,val sortedKey:List<Float>,val sortedValue:List<String>) :
    RecyclerView.Adapter<DieasesAccuracyAdapter.DieasesAccuracyViewHolder>() {
    private lateinit var dieaseslabel:String
    inner class DieasesAccuracyViewHolder(val binding: DieasesAccuracyCardBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieasesAccuracyViewHolder {
        val binding = DieasesAccuracyCardBinding.inflate(LayoutInflater.from(context),parent,false)
        return DieasesAccuracyViewHolder(binding)
    }

    override fun getItemCount() = sortedValue.size

    override fun onBindViewHolder(holder: DieasesAccuracyViewHolder, position: Int) {
        val fileName = "dieaseslabels.txt"
        val inputString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        var dieasesLabelList = inputString.split("\n")
        holder.binding.dieaseslabel.text = sortedValue[position]
        holder.binding.dieasesaccuracy.setText("${sortedKey[position]}%")
        // Set Button Dieases Information
         holder.binding.btnInfo.setOnClickListener {
             dieaseslabel = holder.binding.dieaseslabel.text.toString()
             val index = dieasesLabelList.indexOf(dieaseslabel)
             val dieasesInfoIntent = Intent(context,DieasesInformation_Activity::class.java)
             dieasesInfoIntent.putExtra("urlIndex",index)
             dieasesInfoIntent.putExtra("requestCode","dieasesInformation")
             context.startActivity(dieasesInfoIntent)
         }

        holder.binding.btnAppointment.setOnClickListener {
            dieaseslabel = holder.binding.dieaseslabel.text.toString()
            val appointmentIntent = Intent(context, MainActivity::class.java)
            appointmentIntent.putExtra("OpenScreen","doctorAppointment")
            appointmentIntent.putExtra("dieases",dieaseslabel)
            context.startActivity(appointmentIntent)
        }
    }
}
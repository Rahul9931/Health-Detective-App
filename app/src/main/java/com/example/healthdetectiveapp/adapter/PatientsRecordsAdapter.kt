package com.example.healthdetectiveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.PatientsFilesRecordsBinding
import com.example.healthdetectiveapp.model.PatientsRecordsModel

class PatientsRecordsAdapter(private val context: Context,private val RecordsList:List<PatientsRecordsModel>):
    RecyclerView.Adapter<PatientsRecordsAdapter.PatientsRecordsViewHolder>() {
    private var lastPosition = -1
    inner class PatientsRecordsViewHolder(val binding:PatientsFilesRecordsBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientsRecordsViewHolder {
        val binding = PatientsFilesRecordsBinding.inflate(LayoutInflater.from(context),parent,false)
        return PatientsRecordsViewHolder(binding)
    }

    override fun getItemCount()=RecordsList.size

    override fun onBindViewHolder(holder: PatientsRecordsViewHolder, position: Int) {
        setAnimation(holder.itemView,position)
        holder.binding.dateRecords.text = RecordsList[position].date
        holder.binding.rvRecords.layoutManager = LinearLayoutManager(context)
        holder.binding.rvRecords.adapter = FileInformationAdapter(context,RecordsList[position].fileInfo)
    }

    private fun setAnimation(view: View, position: Int){
        if (position>lastPosition){
            var slidAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_anim)
            view.startAnimation(slidAnim)
            lastPosition = position
        }

    }
}
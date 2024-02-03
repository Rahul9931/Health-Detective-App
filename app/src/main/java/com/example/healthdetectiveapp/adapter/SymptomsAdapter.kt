package com.example.healthdetectiveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healthdetectiveapp.databinding.SymptomsCardBinding

class SymptomsAdapter(
    val context:Context,
    val symList:MutableList<String>
): RecyclerView.Adapter<SymptomsAdapter.SymptomsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomsViewHolder {
        val binding = SymptomsCardBinding.inflate(LayoutInflater.from(context),parent,false)
        return SymptomsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return symList.size
    }

    override fun onBindViewHolder(holder: SymptomsViewHolder, position: Int) {
        holder.binding.symptoms.text = symList[position]
        holder.binding.clearSymptoms.setOnClickListener {
            symList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyItemRangeChanged(holder.adapterPosition, itemCount)
        }
    }
    inner class SymptomsViewHolder(val binding: SymptomsCardBinding):RecyclerView.ViewHolder(binding.root) {

    }
}
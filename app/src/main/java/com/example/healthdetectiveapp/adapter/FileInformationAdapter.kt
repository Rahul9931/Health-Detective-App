package com.example.healthdetectiveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthdetectiveapp.databinding.FilesCardBinding
import com.example.healthdetectiveapp.model.FileInformationModel

class FileInformationAdapter(private val context: Context,private var fileinfoList:List<FileInformationModel>):
    RecyclerView.Adapter<FileInformationAdapter.FileInformationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FileInformationAdapter.FileInformationViewHolder {
        val binding = FilesCardBinding.inflate(LayoutInflater.from(context),parent,false)
        return FileInformationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FileInformationAdapter.FileInformationViewHolder,
        position: Int
    ) {

        holder.binding.filenameFilecard.text = fileinfoList[position].filename
    }

    override fun getItemCount() = fileinfoList.size

    inner class FileInformationViewHolder(val binding:FilesCardBinding):RecyclerView.ViewHolder(binding.root) {

    }
}
package com.example.healthdetectiveapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.UploadFilesDialogCardBinding

class UploadFilesAdapter(
    val files:List<String>,
    val status:List<String>,
    val fileSize:List<String>
): RecyclerView.Adapter<UploadFilesAdapter.UploadFileViewHolder>() {
    private var lastPosition = -1
    inner class UploadFileViewHolder(val binding: UploadFilesDialogCardBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadFileViewHolder {
        val binding = UploadFilesDialogCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UploadFileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onBindViewHolder(holder: UploadFileViewHolder, position: Int) {
        setAnimation(holder.itemView,position)
        // Set name
        var filename = files.get(position)
        if (filename.length>15){
            filename = filename.substring(0,15)+"..."
        }
        holder.binding.filename.text = filename
        // Set image
        var filestatus = status.get(position)
        if (filestatus.equals("loading")){
            holder.binding.status.setImageResource(R.drawable.loading)
        }
        else{
            holder.binding.status.setImageResource(R.drawable.checked)
        }

        // Set File Size
        holder.binding.fileSize.text = fileSize.get(position)

    }

    private fun setAnimation(view: View, position: Int){
        if (position>lastPosition){
            var slidAnim = AnimationUtils.loadAnimation(view.context, R.anim.slide_in_anim)
            view.startAnimation(slidAnim)
            lastPosition = position
        }

    }
}
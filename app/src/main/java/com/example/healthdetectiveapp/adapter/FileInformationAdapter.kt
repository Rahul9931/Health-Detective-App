package com.example.healthdetectiveapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthdetectiveapp.DieasesInformation_Activity
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.FilesCardBinding
import com.example.healthdetectiveapp.model.FileInformationModel

class FileInformationAdapter(private val context: Context,private var fileinfoList:List<FileInformationModel>):
    RecyclerView.Adapter<FileInformationAdapter.FileInformationViewHolder>() {
    private var lastPosition = -1

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
        setAnimation(holder.itemView,position)
        var extensionMap = hashMapOf<String,Int>(
            "pdf" to R.drawable.pdf,
            "docx" to R.drawable.docx,
            "7z" to R.drawable.z7,
            "rar" to R.drawable.rar,
            "xls" to R.drawable.xls,
            "txt" to R.drawable.txt,
            "xlsx" to R.drawable.xlsx,
            "ppt" to R.drawable.ppt,
        )
        val file = fileinfoList[position]
        var filename = file.filename
        if (filename!!.length>15){
            filename = filename.substring(0,15)+"..."
        }
        holder.binding.filenameFilecard.text = filename
        holder.binding.filesizeFilecard.text = file.filesize
        if (file.filemime?.startsWith("image")!!){
            Glide.with(context).load(Uri.parse(file.fileUrl)).into(holder.binding.fileimageFilescard)
        }
        else{
            val list = extensionMap.keys.filter {
                it == file.fileextension
            }
            var ext:String
            if (list.isEmpty()){
                ext = R.drawable.defaultimg.toString()
            } else{
                ext = extensionMap[file.fileextension].toString()
            }
            holder.binding.fileimageFilescard.setImageResource(ext!!.toInt())
        }
        holder.binding.filecontainerFilecard.setOnClickListener {
            val showDocument = Intent(context,DieasesInformation_Activity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                Pair.create(holder.binding.fileimageFilescard,"file_image"),
            )
            showDocument.putExtra("fileurl",file.fileUrl)
            showDocument.putExtra("filename", file.filename)
            showDocument.putExtra("mimetype",file.filemime)
            showDocument.putExtra("requestCode","opendocument")
            context.startActivity(showDocument,options.toBundle())
        }
    }

    override fun getItemCount() = fileinfoList.size

    inner class FileInformationViewHolder(val binding:FilesCardBinding):RecyclerView.ViewHolder(binding.root) {

    }

    private fun setAnimation(view: View, position: Int){
        if (position>lastPosition){
            var slidAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_anim)
            view.startAnimation(slidAnim)
            lastPosition = position
        }

    }
}
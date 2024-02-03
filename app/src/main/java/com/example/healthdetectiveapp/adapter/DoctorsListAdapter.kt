package com.example.healthdetectiveapp.adapter

import android.app.Activity
import android.app.ActivityOptions
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
import com.example.healthdetectiveapp.Doctors_Information_Activity
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.DoctorsCardBinding
import com.example.healthdetectiveapp.model.doctors

class DoctorsListAdapter(
    private var context:Context,
    private val doctorsList: ArrayList<doctors>
): RecyclerView.Adapter<DoctorsListAdapter.DoctorsListViewHolder>() {
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsListViewHolder {
        val binding = DoctorsCardBinding.inflate(LayoutInflater.from(context),parent,false)
        return DoctorsListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return doctorsList.size
    }

    override fun onBindViewHolder(holder: DoctorsListViewHolder, position: Int) {
        setAnimation(holder.itemView,position)
        holder.binding.doctorname.text = doctorsList[position].doctorsname
        holder.binding.specialist.text = doctorsList[position].specilist
        holder.binding.rate.text = doctorsList[position].rating
        val uriString = doctorsList[position].doctorimages
        val uri = Uri.parse(uriString)
        Glide.with(context).load(uri).into(holder.binding.doctorimage)

        holder.binding.doctorcard.setOnClickListener {
            val doctorCardIntent = Intent(context,Doctors_Information_Activity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                Pair.create(holder.binding.doctorimage,"img_doctor"),
                Pair.create(holder.binding.doctorname,"txt_doctor_name"),
                Pair.create(holder.binding.specialist,"txt_specialist"),
                Pair.create(holder.binding.rate,"txt_rating"),
                )
            doctorCardIntent.putExtra("doctorname",doctorsList[position].doctorsname)
            doctorCardIntent.putExtra("speciality",doctorsList[position].specilist)
            doctorCardIntent.putExtra("noofPatients",doctorsList[position].noOfPatients)
            doctorCardIntent.putExtra("experience",doctorsList[position].experience)
            doctorCardIntent.putExtra("rating",doctorsList[position].rating)
            doctorCardIntent.putExtra("aboutus",doctorsList[position].aboutus)
            doctorCardIntent.putExtra("workingat",doctorsList[position].workingat)
            doctorCardIntent.putExtra("doctorimage",doctorsList[position].doctorimages)
            context.startActivity(doctorCardIntent,options.toBundle())
        }
    }
    inner class DoctorsListViewHolder(val binding: DoctorsCardBinding):RecyclerView.ViewHolder(binding.root) {

    }

    private fun setAnimation(view: View, position: Int){
        if (position>lastPosition){
            var slidAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_anim)
            view.startAnimation(slidAnim)
            lastPosition = position
        }

    }
}
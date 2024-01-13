package com.example.healthdetectiveapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.healthdetectiveapp.DieasesDiagnoseByImage
import com.example.healthdetectiveapp.Dieases_Detection_Activity
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        binding.dieasesonsymptoms.setOnClickListener {
            startActivity(Intent(requireContext(),Dieases_Detection_Activity::class.java))
        }

        binding.brainTumarCard.setOnClickListener {
            val brainIntent = Intent(requireContext(),DieasesDiagnoseByImage::class.java)
            brainIntent.putExtra("cardKey",1)
            startActivity(brainIntent)
        }
        binding.AlzimerCard.setOnClickListener {
            val alzimerIntent = Intent(requireContext(),DieasesDiagnoseByImage::class.java)
            alzimerIntent.putExtra("cardKey",2)
            startActivity(alzimerIntent)
        }
        binding.LungsCancerCard.setOnClickListener {
            val lungsCancerIntent = Intent(requireContext(),DieasesDiagnoseByImage::class.java)
            lungsCancerIntent.putExtra("cardKey",3)
            startActivity(lungsCancerIntent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.slider1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider4, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider5, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider6, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setSlideAnimation(AnimationTypes.DEPTH_SLIDE)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {

            }
        })
    }

}
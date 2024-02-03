package com.example.healthdetectiveapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.healthdetectiveapp.databinding.ActivityDieasesDiagnoseByImageBinding
import com.example.healthdetectiveapp.ml.BrainTumor10Epochs
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class DieasesDiagnoseByImage : AppCompatActivity() {
    private val binding:ActivityDieasesDiagnoseByImageBinding by lazy {
        ActivityDieasesDiagnoseByImageBinding.inflate(layoutInflater)
    }
    lateinit var bitmap: Bitmap
    lateinit var status:String
    var classifier: Classifier?=null
    private val CAMERA_REQUEST_CODE = 200
    private var index:Int?=null
    private lateinit var dieases:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val cardvalue = intent.getIntExtra("cardKey",0)
        dieases = intent.getStringExtra("disease")!!
        binding.btnShowResult.isEnabled = false
        binding.txtWarning.setText("Please choose ${dieases} Images for right result otherwise it gives wrong result")
        binding.btnGallery.setOnClickListener {
            binding.resultContainer.visibility = View.GONE
            pickimage.launch("image/*")
        }

        binding.btnShowResult.setOnClickListener {
            binding.resultContainer.visibility = View.VISIBLE
            when(cardvalue){
                1->{
                    status= classifier!!.brainTumar()
                    binding.Result.text = status
                    index = 41
                }
                2->{
                    status= classifier!!.alzimerDetection()
                    binding.Result.text = status
                    index = 42
                }
                3->{
                    status= classifier!!.lungsCancerDetection()
                    binding.Result.text = status
                    index = 43
                }
            }
        }
        // Set Dieases Url Information
        binding.dieasesInfoImage.setOnClickListener {
            val braintumarInfoIntent = Intent(this,DieasesInformation_Activity::class.java)
            braintumarInfoIntent.putExtra("urlIndex",index)
            startActivity(braintumarInfoIntent)
        }

        binding.doctorAppointmentImage.setOnClickListener {
            val appointmentIntent = Intent(this, MainActivity::class.java)
            appointmentIntent.putExtra("OpenScreen","doctorAppointment")
            appointmentIntent.putExtra("dieases",dieases)
            startActivity(appointmentIntent)
        }
// set sample image
        binding.btnSampleImage.setOnClickListener {
            when(cardvalue){
                1->{
                    binding.dieasesImage.setImageResource(R.drawable.brain_tumar)
                }
                2->{
                    binding.dieasesImage.setImageResource(R.drawable.alzimer_sample_image)
                }
                3->{
                    binding.dieasesImage.setImageResource(R.drawable.lungs_cancer_sample)
                }
            }
        }

// set Camera Button
        binding.btnCamera.setOnClickListener {
            capturePhoto()
        }
    }
// Pick Image From Gallery
    val pickimage = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        if (uri!=null){
            binding.btnShowResult.isEnabled = true
            binding.dieasesImage.setImageURI(uri)
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            classifier = Classifier(this,bitmap)
        }
    }
// Take Image From Camera
    fun capturePhoto(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            if (requestCode==CAMERA_REQUEST_CODE){
                binding.btnShowResult.isEnabled = true
                bitmap = data?.extras?.get("data") as Bitmap
                binding.dieasesImage.setImageBitmap(bitmap)
                classifier = Classifier(this,bitmap)
            }
        }
    }
}
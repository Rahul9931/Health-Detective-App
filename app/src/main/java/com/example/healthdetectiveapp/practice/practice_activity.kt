package com.example.healthdetectiveapp.practice

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthdetectiveapp.databinding.ActivityPracticeBinding
import com.example.healthdetectiveapp.model.doctors
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class practice_activity : AppCompatActivity() {
    private val binding: ActivityPracticeBinding by lazy {
        ActivityPracticeBinding.inflate(layoutInflater)
    }
    private val PICK_IMAGES_REQUEST = 100

    // Initialize Firebase
    val storageReference = FirebaseStorage.getInstance().reference
    val databaseReference = FirebaseDatabase.getInstance().getReference("DoctorsList")
    private lateinit var doctorNameList: List<String>
    private lateinit var specialistList: List<String>
    private lateinit var noOfPatientsList: List<String>
    private lateinit var experienceList: List<String>
    private lateinit var ratingList: List<String>
    private lateinit var workingAtList: List<String>
    private lateinit var aboutUsList: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize files
        /*val doctorsNames = "DoctorsName.txt"
        val doctorNameInput =
            application.assets?.open(doctorsNames)?.bufferedReader().use { it?.readText() }
        doctorNameList = doctorNameInput!!.split("\n")

        val specialist = "specialist.txt"
        val specialistInput = application.assets?.open(specialist)?.bufferedReader().use { it?.readText() }
        specialistList = specialistInput!!.split("\n")*/

        val noOfPatients = "noOfPatients.txt"
        val noOfPatientsInput =
            application.assets?.open(noOfPatients)?.bufferedReader().use { it?.readText() }
        noOfPatientsList = noOfPatientsInput!!.split("\n")

        val experience = "Experience.txt"
        val experienceInput =
            application.assets?.open(experience)?.bufferedReader().use { it?.readText() }
        experienceList = experienceInput!!.split("\n")

        val rating = "rating.txt"
        val ratingInput = application.assets?.open(rating)?.bufferedReader().use { it?.readText() }
        ratingList = ratingInput!!.split("\n")

        val workingAt = "workingAt.txt"
        val workingAtInput =
            application.assets?.open(workingAt)?.bufferedReader().use { it?.readText() }
        workingAtList = workingAtInput!!.split("\n")

        /*val aboutUs = "aboutus.txt"
        val aboutUsInput =
            application.assets?.open(aboutUs)?.bufferedReader().use { it?.readText() }
        aboutUsList = aboutUsInput!!.split("\n")*/
        doctorNameList = listOf(
            "Dr. Saanvi Joshi",
            "Dr. Aarya Kumar",
            "Dr. Aaradhya Singhania",
            "Dr. Aanya Chauhan",
            "Dr. Karthik Menon",
            "Dr. Aryan Kapoor",
            "Dr. Akash Joshi",
            "Dr. Yash Rajput"
        )
        specialistList = listOf(
            "nutrionist or a dietitian",
            "bariatricians or obesity specialists",
            "nutrionist or a dietitian",
            "bariatricians or obesity specialists",
            "nutrionist or a dietitian",
            "bariatricians or obesity specialists",
            "nutrionist or a dietitian",
            "bariatricians or obesity specialists"
        )
        aboutUsList = listOf(
            "She believes in the importance of preventive care and educating his patients about their health.",
            "She is also a strong advocate for patient-centered care and involves his patients in all aspects of their treatment.",
            "She takes a holistic approach to care, considering all aspects of a patient's health, including their physical, mental, and emotional well-being.",
            "She believes in building strong relationships with his patients and providing them with the information and support they need to make informed decisions about their health.",
            "He believes in the importance of preventive care and educating his patients about their health.",
            "He is also a strong advocate for patient-centered care and involves his patients in all aspects of their treatment.",
            "He takes a holistic approach to care, considering all aspects of a patient's health, including their physical, mental, and emotional well-being.",
            "He believes in building strong relationships with his patients and providing them with the information and support they need to make informed decisions about their health."
        )

        binding.button.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGES_REQUEST
            )

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            val imagesUriList = mutableListOf<Uri>()
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    imagesUriList.add(imageUri)
                }
            } else if (data?.data != null) {
                val imageUri: Uri = data.data!!
                imagesUriList.add(imageUri)
            }

            // Upload each image to Firebase Storage and save its download URL to Realtime Database
            var j = 0
            for (i in 0..imagesUriList.size - 1) {
                val newItemKey = databaseReference.push().key
                val imageRef = storageReference.child("images/$newItemKey")

                imageRef.putFile(imagesUriList[i])
                    .addOnSuccessListener { taskSnapshot ->
                        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            var doctors = doctors(
                                newItemKey,
                                doctorNameList[i],
                                specialistList[i],
                                ratingList[i],
                                downloadUri.toString(),
                                noOfPatientsList[i],
                                experienceList[i],
                                aboutUsList[i],
                                workingAtList[i],
                                "8810566785"
                            )
                            //val imageInfo = ImageInfo(downloadUri.toString(), "Image Description")
                            databaseReference.child(newItemKey!!).setValue(doctors)
                                .addOnSuccessListener {
                                    j++
                                    Toast.makeText(
                                        this,
                                        "Successful Upload : ${j}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle unsuccessful uploads
                    }
            }
        }
    }

}
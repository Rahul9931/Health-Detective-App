package com.example.healthdetectiveapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.healthdetectiveapp.databinding.ActivityAppointmentBinding
import com.example.healthdetectiveapp.model.PatientModel
import com.google.android.material.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import com.example.healthdetectiveapp.R as r

class Appointment_Activity : AppCompatActivity() {
    private val binding: ActivityAppointmentBinding by lazy {
        ActivityAppointmentBinding.inflate(layoutInflater)
    }
    private val genderList = mutableListOf("Male", "Female", "Other")
    private lateinit var calender: Calendar
    private lateinit var patientName: String
    private lateinit var age: String
    private lateinit var gender: String
    private lateinit var dieases: String
    private lateinit var phoneno: String
    private lateinit var email: String
    private lateinit var address: String
    private lateinit var patientImage: Uri
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private lateinit var database: FirebaseDatabase
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        calender = Calendar.getInstance()
        database = FirebaseDatabase.getInstance()
        val autocompleteAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, genderList)
        val autoComplete = binding.autocompleteAppointment
        autoComplete.setAdapter(autocompleteAdapter)

        // Fetching dieases from dieaseslabels.txt
        val allDieases = "disease_HE"
        val allDieasesInput =
            application.assets.open(allDieases).bufferedReader().use { it.readText() }
        var allDieasesList = allDieasesInput.split("\n")

        val autocompleteAllDieaseseAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, allDieasesList)
        val autoCompleteAllDieases = binding.autocompleteAlldieaseseAppointment
        autoCompleteAllDieases.setAdapter(autocompleteAllDieaseseAdapter)

        // Set Date Button
        binding.date.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, { datePicker, year: Int, month: Int, day: Int ->
                    selectedDate = "$day/${month + 1}/$year"
                    Toast.makeText(this, "selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
                    binding.date.text = selectedDate
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Set Time Button
        binding.time.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(
                this, { timePicker, hour, minute ->
                    selectedTime = "$hour:$minute"
                    Toast.makeText(this, "Selected Time: $selectedTime", Toast.LENGTH_SHORT).show()
                    binding.time.text = selectedTime
                },
                hour, minute, false
            )
            timePicker.show()
        }

        // Set Image
        binding.patientImage.setOnClickListener {
            pickimage.launch("image/*")
        }
        // Set Appointment
        binding.btnSetappointment.setOnClickListener {
            // Getting Patient Information
            patientName = binding.patientname.text.toString().trim()
            age = binding.age.text.toString().trim()
            gender = binding.autocompleteAppointment.text.toString().trim()
            dieases = binding.autocompleteAlldieaseseAppointment.text.toString().trim()
            phoneno = binding.phoneno.text.toString().trim()
            email = binding.email.text.toString().trim()
            address = binding.address.text.toString().trim()
            date = binding.date.text.toString().trim()
            time = binding.time.text.toString().trim()

            // Validate Patient Information
            if (!patientName.isEmpty()) {
                binding.patientname.error = null
                if (!age.isEmpty()) {
                    binding.age.error = null
                    if (!gender.isEmpty()) {
                        binding.autocompleteAppointment.error = null
                        if (!phoneno.isEmpty()) {
                            binding.phoneno.error = null
                            if (phoneno.matches("[1-9][0-9]{9}".toRegex())){
                                binding.phoneno.error = null
                                if (!email.isEmpty()) {
                                    binding.email.error = null
                                    if (email.matches("^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())){
                                        binding.email.error = null
                                        if (!address.isEmpty()) {
                                            binding.address.error = null
                                            if (!selectedDate.isEmpty()) {
                                                binding.date.error = null
                                                if (!selectedTime.isEmpty()) {
                                                    binding.time.error = null
                                                    uploadPatientData()
                                                } else {
                                                    binding.time.error = "Please Select Time"
                                                }
                                            } else {
                                                binding.date.error = "Please Select Date"
                                            }
                                        }
                                        else {
                                            binding.address.error = "Please Enter Address"
                                        }
                                    }
                                    else{
                                        binding.email.error = "Please Fill Valid Email"
                                    }

                                } else {
                                    binding.email.error = "Please Fill Patient Email"
                                }
                            }
                            else{
                                binding.phoneno.error = "Please Fill Valid Phone Number"
                            }
                        } else {
                            binding.phoneno.error = "Please Fill Patient Phone Number"
                        }
                    } else {
                        binding.autocompleteAppointment.error = "Please Select Gender"
                    }
                } else {
                    binding.age.error = "Please Fill Patient Age"
                }
            } else {
                binding.patientname.error = "Please Fill Patient Name"
            }
        }
    }

    private fun uploadPatientData() {
        val userId = auth.currentUser!!.uid
        val patientRef = database.getReference("PatientsList").child(userId)
        val newItemsKey = patientRef.push().key
        if (patientImage!=null){
            binding.progressBar.visibility = View.VISIBLE
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("Patients_Image/${newItemsKey}.jpg")
            val uploadTask = imageRef.putFile(patientImage!!)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadurl->
                    // Assign Model
                    val patientData = PatientModel(newItemsKey,patientName,downloadurl.toString(),age, gender, dieases, phoneno, email, address, date, time)
                    patientRef.child(newItemsKey!!).setValue(patientData).addOnSuccessListener {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Your Appointment is Fix", Toast.LENGTH_SHORT).show()
                        binding.date.text = "Date"
                        binding.time.text = "Time"
                        binding.patientname.text?.clear()
                        binding.age.text?.clear()
                        binding.autocompleteAppointment.text.clear()
                        binding.phoneno.text?.clear()
                        binding.email.text?.clear()
                        binding.address.text?.clear()
                        binding.autocompleteAlldieaseseAppointment.text.clear()
                        binding.patientImage.setImageResource(r.drawable.account)
                    }
                        .addOnFailureListener {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
        else{
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }
    // Pick Image From Gallery
    val pickimage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.patientImage.setImageURI(uri)
            patientImage = uri
        }
    }
}
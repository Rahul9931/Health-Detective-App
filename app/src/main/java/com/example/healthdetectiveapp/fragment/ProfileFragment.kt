package com.example.healthdetectiveapp.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.healthdetectiveapp.R
import com.example.healthdetectiveapp.databinding.FragmentProfileBinding
import com.example.healthdetectiveapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    private lateinit var userRef:DatabaseReference
    private lateinit var userimage:Uri
    private val genderList = mutableListOf("Male", "Female", "Other")
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
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        val autocompleteAdapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, genderList)
        val autoComplete = binding.autocompleteProfile
        autoComplete.setAdapter(autocompleteAdapter)

        // Set User Data
        setUserData()
        // Disable EditText
        binding.apply {
            username.isEnabled = false
            age.isEnabled = false
            autocompleteProfile.isEnabled = false
            address.isEnabled = false
            phoneno.isEnabled = false
            email.isEnabled = false
            passward.isEnabled = false
            readid.isEnabled = false
            userimage.isEnabled = false
        }
        binding.btnEdit.setOnClickListener {
            binding.apply {
                username.isEnabled = !username.isEnabled
                age.isEnabled = !age.isEnabled
                autocompleteProfile.isEnabled = !autocompleteProfile.isEnabled
                address.isEnabled = !address.isEnabled
                phoneno.isEnabled = !phoneno.isEnabled
                email.isEnabled = !email.isEnabled
                passward.isEnabled = !passward.isEnabled
                userimage.isEnabled = !userimage.isEnabled
                binding.btnUpdateProfile.visibility = View.VISIBLE
            }
        }
        binding.userimage.setOnClickListener {
            pickimage.launch("image/*")
        }
        binding.btnUpdateProfile.setOnClickListener {
            binding.apply {
                val name = username.text.toString().trim()
                val age = age.text.toString().trim()
                val gender = autocompleteProfile.text.toString().trim()
                val address = address.text.toString().trim()
                val phone = phoneno.text.toString().trim()
                val email = email.text.toString().trim()
                val passward = passward.text.toString().trim()
                val readid = readid.text.toString().trim()
                updateUserData(name,age,gender,address,phone,email,passward,readid)
            }
        }
        return binding.root
    }

    private fun updateUserData(
        name: String,
        age: String,
        gender: String,
        address: String,
        phone: String,
        email: String,
        passward: String,
        readid:String
    ) {
        // Performing Validation
        if (!name.isEmpty()){
            binding.username.error = null
            if (!phone.isEmpty()){
                binding.phoneno.error = null
                if (!email.isEmpty()){
                    binding.email.error = null
                    if (!passward.isEmpty()){
                        binding.passward.error = null
                        uploadData(name, age, gender, address, phone, email, passward,readid)
                    }
                    else{
                        binding.passward.error ="Please Fill Passward"
                    }
                }
                else{
                    binding.email.error ="Please Fill Email"
                }
            }
            else{
                binding.phoneno.error ="Please Fill Phone Number"
            }
        }
        else{
            binding.username.error ="Please Fill Username"
        }
    }

    private fun uploadData(
        name: String,
        age: String,
        gender: String,
        address: String,
        phone: String,
        email: String,
        passward: String,
        readid: String
    ) {
        // Update Data
        val userId = auth.currentUser?.uid
        userRef = database.getReference("UsersProfile").child(userId!!)
        val storageRef = FirebaseStorage.getInstance().reference
        val userImageRef = storageRef.child("users_images/${userId}.jpg")
        if (userimage!=null){
            val uploadImage = userImageRef.putFile(userimage)
            uploadImage.addOnSuccessListener {
                userImageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->

                    // User model
                    val userData = UserModel(userId, name, age, gender, address, phone, email, passward,readid,downloadUrl.toString())
                    userRef.setValue(userData)
                }
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId!=null){
            userRef = database.getReference("UsersProfile").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val user = snapshot.getValue(UserModel::class.java)
                        if (user!=null){
                            if (user.userimage!=null){
                                Glide.with(requireContext()).load(Uri.parse(user.userimage)).into(binding.userimage)
                            }
                            if (user.age!=null){
                                binding.age.setText(user.age)
                            }
                            if (user.gender!=null){
                                binding.autocompleteProfile.setText(user.gender)
                            }
                            if (user.address!=null){
                                binding.address.setText(user.address)
                            }
                            binding.username.setText(user.name)
                            binding.phoneno.setText(user.phone)
                            binding.email.setText(user.email)
                            binding.passward.setText(user.passward)
                            binding.readid.setText(user.readid)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
    // Pick Image From Gallery
    val pickimage = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        if (uri!=null){
            binding.userimage.setImageURI(uri)
            userimage = uri
        }
    }
}
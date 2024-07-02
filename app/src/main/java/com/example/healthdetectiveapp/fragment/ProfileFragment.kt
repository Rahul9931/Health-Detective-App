package com.example.healthdetectiveapp.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
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
import java.lang.StringBuilder
import java.security.SecureRandom

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    private lateinit var userRef:DatabaseReference
    private var userimage: Uri? =null
    private var gellaryImage = false
    private val genderList = arrayOf("Male", "Female", "Other")
    private lateinit var readId:String
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

        var isVisible=false
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
            readid.isEnabled = false
            userimage.isEnabled = false
            btnUpdateProfile.visibility = View.GONE
        }
        binding.btnEdit.setOnClickListener {
            isVisible = !isVisible
            binding.apply {
                if (isVisible){
                    binding.btnUpdateProfile.visibility = View.VISIBLE
                }
                else{
                    binding.btnUpdateProfile.visibility = View.GONE
                }
                username.isEnabled = !username.isEnabled
                age.isEnabled = !age.isEnabled
                autocompleteProfile.isEnabled = !autocompleteProfile.isEnabled
                address.isEnabled = !address.isEnabled
                phoneno.isEnabled = !phoneno.isEnabled
                email.isEnabled = !email.isEnabled
                userimage.isEnabled = !userimage.isEnabled
                readid.isEnabled = !readid.isEnabled
            }
        }
        binding.userimage.setOnClickListener {
            pickimage.launch("image/*")
        }

        // Set Sync Image Button
        binding.imgbtnSyn.setOnClickListener {
            readId = generatePassword(15)
            binding.readid.setText(readId)
        }

        // Set Copy Image Button
        binding.imgbtnCopy.setOnClickListener {
            copyToClipboard()
            Status("Text Copy to Clipboard")
        }
        binding.btnUpdateProfile.setOnClickListener {
            binding.apply {
                val name = username.text.toString().trim()
                val age = age.text.toString().trim()
                val gender = autocompleteProfile.text.toString().trim()
                val address = address.text.toString().trim()
                val phone = phoneno.text.toString().trim()
                val email = email.text.toString().trim()
                val readid = readid.text.toString().trim()
                updateUserData(name,age,gender,address,phone,email,readid)
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
        readid:String
    ) {
        // Performing Validation
        if (!name.isEmpty()){
            binding.username.error = null
            if (!phone.isEmpty()){
                binding.phoneno.error = null
                if (phone.matches("[1-9][0-9]{9}".toRegex())){
                    binding.phoneno.error = null
                    if (!email.isEmpty()){
                        binding.email.error = null
                        if (email.matches("^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())){
                            binding.email.error = null
                            uploadData(name, age, gender, address, phone, email,readid)
                        }
                        else{
                            binding.email.error = "Please Fill Valid Email"
                        }
                    }
                    else{
                        binding.email.error ="Please Fill Email"
                    }
                }
                else{
                    binding.phoneno.error = "Please Fill Valid Phone Number"
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
        readid: String
    ) {
        // Update Data
        binding.progressBar.visibility = View.VISIBLE
        val userId = auth.currentUser?.uid
        userRef = database.getReference("UsersProfile").child(userId!!)
        val storageRef = FirebaseStorage.getInstance().reference
        val userImageRef = storageRef.child("users_images/${userId}.jpg")
        if (userimage!=null){
            if (gellaryImage){
                val uploadImage = userImageRef.putFile(userimage!!)
                uploadImage.addOnSuccessListener {
                    userImageRef.downloadUrl.addOnSuccessListener {
                            downloadUrl->

                        // User model
                        val userData1 = UserModel(userId, name, age, gender, address, phone, email,readid,downloadUrl.toString())
                        userRef.setValue(userData1).addOnSuccessListener {
                            binding.progressBar.visibility = View.GONE
                            Status("Profile Successfully Update")
                        }
                            .addOnFailureListener {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            else{
                // User model
                val userData1 = UserModel(userId, name, age, gender, address, phone, email,readid,userimage.toString())
                userRef.setValue(userData1).addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                    Status("Profile Successfully Update")
                }
                    .addOnFailureListener {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show()
                    }
            }


        }
        else{
            // User model
            val userData2 = UserModel(userId, name, age, gender, address, phone, email,readid,userimage = null)
            userRef.setValue(userData2).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Status("Profile Successfully Update")
            }
                .addOnFailureListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId!=null){
            userRef = database.getReference("UsersProfile").child(userId)
            userRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val user = snapshot.getValue(UserModel::class.java)
                        if (user!=null){
                            if (user.userimage!=null){
                                userimage = Uri.parse(user.userimage)
                                Glide.with(binding.userimage.context).load(Uri.parse(user.userimage)).into(binding.userimage)
                            }
                            else{
                                binding.userimage.setImageResource(R.drawable.account)
                            }
                            if (user.age!=null){
                                binding.age.setText(user.age)
                            }
                            if (user.gender!=null){
                                binding.autocompleteProfile.setText(user.gender,false)
                            }
                            if (user.address!=null){
                                binding.address.setText(user.address)
                            }
                            binding.username.setText(user.name)
                            binding.phoneno.setText(user.phone)
                            binding.email.setText(user.email)
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
            gellaryImage = true
        }
    }
    fun Status(s1:String){
        Toast.makeText(requireContext(), "${s1}", Toast.LENGTH_SHORT).show()
    }

    fun generatePassword(length:Int):String{
        val charSet = ('a'..'z')+('A'..'Z')+(0..9)+ listOf('!','@','#','$','%','^','&','*','?','+','-','/')
        val secureRandom = SecureRandom()
        val password = StringBuilder()
        repeat(length,{
            val charIndex = secureRandom.nextInt(charSet.size)
            password.append(charSet[charIndex])
        })
        return password.toString()
    }

    fun copyToClipboard(){
        val copy = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("readid",binding.readid.text)
        copy.setPrimaryClip(clip)
    }
}
package com.example.healthdetectiveapp.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthdetectiveapp.adapter.UploadFilesAdapter
import com.example.healthdetectiveapp.databinding.FragmentUploadRecordBinding
import com.example.healthdetectiveapp.model.FileInformationModel
import com.example.healthdetectiveapp.model.PatientsRecordsModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.Calendar

class uploadRecordFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentUploadRecordBinding
    private lateinit var files: ArrayList<String>
    private lateinit var status: ArrayList<String>
    private lateinit var uploadFileAdapter: UploadFilesAdapter
    private val PICK_FILE_REQUEST = 101
    private lateinit var imageUri: Uri
    private lateinit var contentResolver: ContentResolver
    val storageReference = FirebaseStorage.getInstance().reference
    val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var patientsRecordsList : ArrayList<PatientsRecordsModel> = ArrayList()
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
        binding = FragmentUploadRecordBinding.inflate(inflater, container, false)
        files = arrayListOf()
        status = arrayListOf()
        binding.rvRecord.layoutManager = LinearLayoutManager(requireContext())
        uploadFileAdapter = UploadFilesAdapter(files, status)
        binding.rvRecord.adapter = uploadFileAdapter

        // Pick File From Local Storage
        binding.btnselectfiles.setOnClickListener {
            files.clear()
            status.clear()
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(
                Intent.createChooser(intent, "Select Files via"),
                PICK_FILE_REQUEST
            )
        }
        binding.btndismiss.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            contentResolver = requireContext().contentResolver
            if (data?.clipData != null) {
                Toast.makeText(requireContext(), "clipData", Toast.LENGTH_LONG).show()
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    imageUri = data.clipData!!.getItemAt(i).uri
                    val mimeType = contentResolver.getType(imageUri)
                    if (mimeType!!.startsWith("audio")) {
                        Toast.makeText(
                            requireContext(),
                            "audio files are not upload",
                            Toast.LENGTH_SHORT
                        ).show()
                        break
                    } else if (mimeType!!.startsWith("video")) {
                        Toast.makeText(
                            requireContext(),
                            "video files are not upload",
                            Toast.LENGTH_SHORT
                        ).show()
                        break
                    } else {
                        val filename = getFileNameFromUri(imageUri)
                        Log.d("test_filename1", "${filename}")
                        val fileextension = getFileExtensionFromUri(imageUri)
                        Log.d("test_fileext1", "${fileextension}")
                        val filesize = getFileSizeFromUri(imageUri)
                        Log.d("test_filesize1", "${filesize}")

                        // Get Current Date and Time
                        val calendar = Calendar.getInstance().time
                        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)
                        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)

                        files.add(filename!!)
                        status.add("loading")
                        uploadFileAdapter.notifyDataSetChanged()
                        val index = i
                        val fileRef = storageReference.child("patientsRecord/$filename")
                        fileRef.putFile(imageUri)
                            .addOnSuccessListener {
                                if (mimeType.startsWith("image")){
                                    fileRef.downloadUrl.addOnSuccessListener { downloadurl ->
                                        uploadPatientsRecords(downloadurl,fileRef,filename,filesize,fileextension,dateFormat,timeFormat)
                                    }
                                }
                                else{
                                    uploadPatientsRecords(downloadurl=null,fileRef,filename,filesize,fileextension,dateFormat,timeFormat)
                                }
                                status.removeAt(index)
                                status.add(index,"done")
                                uploadFileAdapter.notifyDataSetChanged()

                            }
                            .addOnFailureListener{
                                Log.d("uploadfail","${it.message}")
                                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            } else if (data?.data != null) {
                Toast.makeText(requireContext(), "data", Toast.LENGTH_SHORT).show()
                imageUri = data.data!!
                val mimeType = contentResolver.getType(imageUri)
                Log.d("test_mimetype", "${mimeType}")
                if (mimeType!!.startsWith("audio")) {
                    Toast.makeText(
                        requireContext(),
                        "audio files are not upload",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (mimeType!!.startsWith("video")) {
                    Toast.makeText(
                        requireContext(),
                        "video files are not upload",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val filename = getFileNameFromUri(imageUri)
                    Log.d("test_filename2", "${filename}")
                    val fileextension = getFileExtensionFromUri(imageUri)
                    Log.d("test_fileext2", "${fileextension}")
                    val filesize = getFileSizeFromUri(imageUri)
                    Log.d("test_filesize2", "${filesize}")

                    // Get Current Date and Time
                    val calendar = Calendar.getInstance().time
                    val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)
                    val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)

                    Log.d("test_date","$dateFormat")
                    Log.d("test_time","$timeFormat")
                    files.add(filename!!)
                    status.add("loading")
                    uploadFileAdapter.notifyDataSetChanged()
                    val fileRef = storageReference.child("patientsRecord/$filename")
                    fileRef.putFile(imageUri)
                        .addOnSuccessListener {
                                if (mimeType.startsWith("image")){
                                    fileRef.downloadUrl.addOnSuccessListener { downloadurl ->
                                        uploadPatientsRecords(downloadurl,fileRef,filename,filesize,fileextension,dateFormat,timeFormat)
                                    }
                                }
                                else{
                                    uploadPatientsRecords(downloadurl=null,fileRef,filename,filesize,fileextension,dateFormat,timeFormat)
                                }
                            status.remove("loading")
                            status.add("done")
                            uploadFileAdapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener{
                            Log.d("uploadfail","${it.message}")
                            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                Log.d("test_typeList2", "${mimeType}")
            }
            // Upload each file to Firebase Storage and save its download URL to Realtime Database

        }
    }

    private fun uploadPatientsRecords(
        downloadurl: Uri?,
        fileRef: StorageReference,
        fileName: String,
        filesize: Double,
        fileextension: String,
        dateFormat: String,
        timeFormat: String
    ) {
        // Prepare Data                                                                         
        val fileInfo = listOf(
            FileInformationModel(fileName,filesize.toString(),fileextension,downloadurl.toString(),timeFormat)
        )
        val patientRecord = PatientsRecordsModel(dateFormat,fileInfo)
        // Get Patients Records Reference
        val patientsRecordsRef = database.reference.child("PatientsRecords")
        // Get User id
        val userid = auth.currentUser!!.uid
        // Fetch Patients Record for comparision
        val dateKeyRef = patientsRecordsRef.child(userid).child(dateFormat)
        /*dateKeyRef.setValue(dateKey)
        dateKeyRef.child("fileinformationlist").push().setValue(fileInfo)*/
        dateKeyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Toast.makeText(requireContext(), "Equal Date", Toast.LENGTH_SHORT).show()
                    val fileInformationRef = patientsRecordsRef.child(userid).child(dateFormat).child("fileinformaionlist")
                    fileInformationRef.push().setValue(fileInfo).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Successfully Upload", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Upload Fail", Toast.LENGTH_SHORT).show()
                            Log.d("uploadRecordFragment_upload fail","${it.message}")
                        }

                    }
                else{
                    patientsRecordsRef.child(userid).child(dateFormat).setValue(patientRecord).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Successfully Upload", Toast.LENGTH_SHORT).show()
                        /*val fileInformationRef = patientsRecordsRef.child(userid).child(dateFormat).child("fileinformaionlist")
                        fileInformationRef.push().setValue(fileInfo).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Successfully Upload", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Upload Fail", Toast.LENGTH_SHORT).show()
                                Log.d("uploadRecordFragment_upload fail","${it.message}")
                            }*/
                    }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Upload Fail", Toast.LENGTH_SHORT).show()
                            Log.d("test_uploadfail","${it.message}")
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val cursor = contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                fileName =
                    it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
            }
        }
        cursor?.close()
        return fileName
    }

    private fun getFileExtensionFromUri(uri: Uri): String {
        val fileName = getFileNameFromUri(uri)
        val extension = fileName?.substring(fileName.lastIndexOf(".") + 1)
        return extension.toString()
    }

    private fun getFileSizeFromUri(uri: Uri): Double {
        // Open a file descriptor for the URI
        val fileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
        // get the file size from fileDescriptor
        var fileSize: Long = fileDescriptor!!.statSize
        // Format size
        val decimalFormat = DecimalFormat("#.##")
        val fileSizeInKB = fileSize.toDouble()/1024.0
        val fileSizeInMB = fileSizeInKB/1024.0
        // close the file descriptor when done
        fileDescriptor?.close()
        return decimalFormat.format(fileSizeInMB).toDouble()
    }

}
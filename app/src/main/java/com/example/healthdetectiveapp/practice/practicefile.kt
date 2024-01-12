package com.example.healthdetectiveapp.practice

import android.net.Uri
import com.example.healthdetectiveapp.R

// Image Uri = https://firebasestorage.googleapis.com/v0/b/healthdetectiveapp-6fc77.appspot.com/o/users_images%2FwBlwHNa6labN6pdrCwyi9fVczbk1.jpg?alt=media&token=45bde989-5bdd-4cc8-a9fa-290b3045847a
fun main(){
    var extensionMap = hashMapOf<String,Int>(
        "pdf" to R.drawable.pdf,
        "docx" to R.drawable.docx
    )
    val list = extensionMap.keys.filter {
        it == "ppt"
    }
    print(list)
}

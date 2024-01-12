package com.example.healthdetectiveapp.model

data class FileInformationModel(
    val filename:String?="",
    val filesize:String?="",
    val fileextension:String?="",
    val filemime:String?="",
    val fileUrl:String?="",
    val isSelected:Boolean=false,
    val time:String?="",

)

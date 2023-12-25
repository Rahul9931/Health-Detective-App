package com.example.healthdetectiveapp.model

data class PatientsRecordsModel(
    var date: String? = "",
    val fileInfo: List<FileInformationModel>,
)

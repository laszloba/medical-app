package com.vaslufi.medicalapp.domain.model

data class Patient(
    val name: String,
    val age: Int,
    val diagnosis: Set<Diagnosis>,
)

package com.vaslufi.medicalapp.domain.model

data class MedicationAdministration(
    val medication: Medication,
    val dosage: Dosage,
    val applicationMethod: ApplicationMethod,
)

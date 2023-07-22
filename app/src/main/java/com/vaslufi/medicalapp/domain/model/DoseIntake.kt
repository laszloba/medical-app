package com.vaslufi.medicalapp.domain.model

import org.joda.time.DateTime

data class DoseIntake(
    val medicationAdministration: MedicationAdministration,
    val timeOfIntake: DateTime,
)

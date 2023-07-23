package com.vaslufi.medicalapp.ui.home

import com.vaslufi.medicalapp.domain.model.Patient

data class DebugInformation(
    val patient: Patient,
    val virtualTime: String,
    val history: String,
) {
    companion object {
        val EMPTY = DebugInformation(
            patient = Patient(
                name = "",
                age = 0,
                diagnosis = emptySet(),
            ),
            virtualTime = "",
            history = "",
        )
    }
}

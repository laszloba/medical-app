package com.vaslufi.medicalapp.domain.model

sealed class DoseSuggestion {
    data class ExactDose(
        val dosage: Dosage,
    ) : DoseSuggestion()
}

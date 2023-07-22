package com.vaslufi.medicalapp.domain.model

sealed class DoseSuggestion {
    data class ExactDose(
        val dosage: Dosage,
    ) : DoseSuggestion()

    data class DoseRange(
        val minimumDosage: Dosage,
        val maximumDosage: Dosage,
    ) : DoseSuggestion()
}

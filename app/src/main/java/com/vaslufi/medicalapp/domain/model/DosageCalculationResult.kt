package com.vaslufi.medicalapp.domain.model

sealed class DosageCalculationResult {
    object MaxLimitReached : DosageCalculationResult()
    object AgeNotSupported : DosageCalculationResult()

    data class TakeDose(val doseSuggestion: DoseSuggestion) : DosageCalculationResult()
}

package com.vaslufi.medicalapp.domain.model

sealed class Advice {
    object NoTreatmentNeeded : Advice()
    object StopTreatment : Advice()
    object SeekMedicalAttention : Advice()
    object MaxLimitReached : Advice()
    object TooEarlyCheckIn : Advice()
    data class TakeDose(val doseSuggestion: DoseSuggestion) : Advice()
}

package com.vaslufi.medicalapp.ui.home

sealed class HomeState {
    object Start : HomeState()
    object StopTreatment : HomeState()
    object NoTreatmentNeeded : HomeState()
    object SeekMedicalAttention : HomeState()
    data class TooEarlyCheckIn(
        val formattedRemainingTime: String,
        val formattedUntilTime: String,
    ) : HomeState()

    data class DosageRecorded(
        val formattedRemainingTime: String,
        val formattedUntilTime: String,
    ) : HomeState()

    object MaxLimitReached : HomeState()
    data class TakeDose(
        val minimumAmount: Double,
        val maximumAmount: Double,
        val formattedDose: String,
    ) : HomeState()
}

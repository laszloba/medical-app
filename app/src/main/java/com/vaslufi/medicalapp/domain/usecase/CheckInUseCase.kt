package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.Advice
import com.vaslufi.medicalapp.domain.model.Advice.MaxLimitReached
import com.vaslufi.medicalapp.domain.model.Advice.NoTreatmentNeeded
import com.vaslufi.medicalapp.domain.model.Advice.SeekMedicalAttention
import com.vaslufi.medicalapp.domain.model.Advice.StopTreatment
import com.vaslufi.medicalapp.domain.model.Advice.TakeDose
import com.vaslufi.medicalapp.domain.model.Advice.TooEarlyCheckIn
import com.vaslufi.medicalapp.domain.model.CheckInData
import com.vaslufi.medicalapp.domain.model.DosageCalculationResult
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.Patient
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Perform the check-in process for the patient and provide advice based on the check-in data.
 */
interface CheckInUseCase {

    /**
     * Run this use case.
     *
     * @param patient The patient for whom the check-in is being performed.
     * @param history The list of previous dose intakes for the patient.
     * @param checkInData The data related to the current check-in.
     * @param currentTime The current date and time of the check-in.
     * @return The advice provided based on the check-in data and patient's history.
     *         Possible advice values:
     *         - [Advice.SeekMedicalAttention]: If the patient needs to seek medical attention.
     *         - [Advice.StopTreatment]: If the patient is feeling better and should stop treatment.
     *         - [Advice.TooEarlyCheckIn]: If the patient checks in too early based on the history.
     *         - [Advice.MaxLimitReached]: If the patient has reached the maximum limit for dosage intake.
     *         - [Advice.TakeDose]: If the patient should take a dose, with the suggested dosage value.
     */
    operator fun invoke(
        patient: Patient,
        history: List<DoseIntake>,
        checkInData: CheckInData,
        currentTime: DateTime,
    ): Advice
}

class CheckInUseCaseImpl @Inject constructor(
    private val checkLowTemperatureUseCase: CheckLowTemperatureUseCase,
    private val checkHighTemperatureUseCase: CheckHighTemperatureUseCase,
    private val checkCheckInTimeTooEarlyUseCase: CheckCheckInTimeTooEarlyUseCase,
    private val calculateDosageUseCase: CalculateDosageUseCase,
) : CheckInUseCase {

    override operator fun invoke(
        patient: Patient,
        history: List<DoseIntake>,
        checkInData: CheckInData,
        currentTime: DateTime
    ): Advice =
        when {
            checkHighTemperatureUseCase(checkInData.temperature) -> SeekMedicalAttention
            checkInData.feelingBetter -> StopTreatment
            checkLowTemperatureUseCase(checkInData.temperature) -> NoTreatmentNeeded
            checkCheckInTimeTooEarlyUseCase(checkInData.time, history) -> TooEarlyCheckIn
            else -> when (
                val dosageCalculationResult = calculateDosageUseCase(patient, history, currentTime)
            ) {
                DosageCalculationResult.AgeNotSupported -> SeekMedicalAttention
                DosageCalculationResult.MaxLimitReached -> MaxLimitReached
                is DosageCalculationResult.TakeDose ->
                    TakeDose(dosageCalculationResult.doseSuggestion)
            }
        }
}

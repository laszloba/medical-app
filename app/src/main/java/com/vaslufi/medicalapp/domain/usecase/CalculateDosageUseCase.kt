package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.DosageCalculationResult
import com.vaslufi.medicalapp.domain.model.DosageCalculationResult.AgeNotSupported
import com.vaslufi.medicalapp.domain.model.DosageCalculationResult.MaxLimitReached
import com.vaslufi.medicalapp.domain.model.DosageCalculationResult.TakeDose
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.Patient
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Calculate the dosage for the patient based on their age, medical history, and current time.
 */
interface CalculateDosageUseCase {

    /**
     * Run this use case.
     *
     * @param patient The patient for whom the dosage needs to be calculated.
     * @param history The list of dose intakes for the patient's medical history.
     * @param currentTime The current date and time to be used for dosage calculation.
     * @return The [DosageCalculationResult], which indicates the recommended dosage or any specific issue.
     *         Possible results include:
     *         - [DosageCalculationResult.AgeNotSupported]: If the patient's age is not supported for dosage calculation.
     *         - [DosageCalculationResult.MaxLimitReached]: If the maximum dosage limit has been reached based on the patient's history.
     *         - [DosageCalculationResult.TakeDose]: If the dosage calculation is successful and the patient can take the recommended dose.
     *             This result contains the calculated dose amount.
     */
    operator fun invoke(
        patient: Patient,
        history: List<DoseIntake>,
        currentTime: DateTime,
    ): DosageCalculationResult
}

class CalculateDosageUseCaseImpl @Inject constructor(
    private val calculateRemainingDoseUseCase: CalculateRemainingDoseUseCase,
    private val checkAgeEligibilityUseCase: CheckAgeEligibilityUseCase,
    private val checkSevereConditionsUseCase: CheckSevereConditionsUseCase,
    private val calculateLimitedDosageUseCase: CalculateLimitedDosageUseCase,
    private val calculateDoseRangeUseCase: CalculateDoseRangeUseCase,
) : CalculateDosageUseCase {

    override operator fun invoke(
        patient: Patient,
        history: List<DoseIntake>,
        currentTime: DateTime
    ): DosageCalculationResult {
        if (!checkAgeEligibilityUseCase(patient.age)) return AgeNotSupported
        val remainingDose = calculateRemainingDoseUseCase(currentTime, history)
        if (remainingDose == 0.0) return MaxLimitReached
        return TakeDose(
            when {
                patient.age in 10..11 || checkSevereConditionsUseCase(patient.diagnosis) ->
                    calculateLimitedDosageUseCase(
                        limit = 500.0,
                        remainingDose = remainingDose,
                    )

                patient.age == 12 ->
                    calculateLimitedDosageUseCase(
                        limit = 625.0,
                        remainingDose = remainingDose,
                    )

                else ->
                    calculateDoseRangeUseCase(
                        minimumAmount = 500.0,
                        maximumAmount = 1000.0,
                        remainingDose = remainingDose,
                    )
            }
        )
    }
}

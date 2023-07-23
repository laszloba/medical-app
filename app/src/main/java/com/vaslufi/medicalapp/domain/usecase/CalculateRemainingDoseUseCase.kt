package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.DoseIntake
import org.joda.time.DateTime
import javax.inject.Inject
import kotlin.math.max

/**
 * Calculate the remaining dosage that can be administered to a patient based on their dosage intake history.
 */
interface CalculateRemainingDoseUseCase {

    /**
     * Run this use case.
     *
     * @param currentTime The current date and time used for the calculation.
     * @param history The list of dose intakes for the patient's medical history.
     * @return The remaining dosage amount that can be administered to the patient.
     */
    operator fun invoke(
        currentTime: DateTime,
        history: List<DoseIntake>,
    ): Double
}

class CalculateRemainingDoseUseCaseImpl @Inject constructor(
    private val calculateTotalDoseInLast24HoursUseCase: CalculateTotalDoseInLast24HoursUseCase,
) : CalculateRemainingDoseUseCase {

    override fun invoke(currentTime: DateTime, history: List<DoseIntake>): Double {
        val totalDoseInLast24Hours = calculateTotalDoseInLast24HoursUseCase(currentTime, history)
        return max(MAXIMUM_DOSE - totalDoseInLast24Hours, 0.0)
    }
}

private const val MAXIMUM_DOSE = 4000.0

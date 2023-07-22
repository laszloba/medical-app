package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.DoseIntake
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Calculate the total dosage intake in the last 24 hours.
 */
interface CalculateTotalDoseInLast24HoursUseCase {

    /**
     * Run this use case.
     *
     * @param currentTime The current date and time used for the calculation.
     * @param history The list of dose intakes for the patient's medical history.
     * @return The total dosage intake in the last 24 hours.
     */
    operator fun invoke(
        currentTime: DateTime,
        history: List<DoseIntake>,
    ): Double
}

class CalculateTotalDoseInLast24HoursUseCaseImpl @Inject constructor() :
    CalculateTotalDoseInLast24HoursUseCase {

    override operator fun invoke(currentTime: DateTime, history: List<DoseIntake>): Double {
        val twentyFourHoursAgo = currentTime.minusHours(TWENTY_FOUR_HOURS)
        return history
            .filter {
                it.timeOfIntake.isAfter(twentyFourHoursAgo)
                        || it.timeOfIntake.isEqual(twentyFourHoursAgo)
            }
            .sumOf { it.medicationAdministration.dosage.amount }
    }
}

private const val TWENTY_FOUR_HOURS = 24

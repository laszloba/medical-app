package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseSuggestion
import javax.inject.Inject
import kotlin.math.min

/**
 * Calculate the dosage suggestion when the remaining dose is within the specified limit.
 */
interface CalculateLimitedDosageUseCase {

    /**
     * Run this use case.
     *
     * @param limit The maximum dosage limit that can be administered to the patient.
     * @param remainingDose The remaining dose that can be administered to the patient.
     * @return A [DoseSuggestion.ExactDose] object representing the exact dosage suggestion within the specified limit.
     */
    operator fun invoke(limit: Double, remainingDose: Double): DoseSuggestion.ExactDose
}

class CalculateLimitedDosageUseCaseImpl @Inject constructor() : CalculateLimitedDosageUseCase {
    override fun invoke(limit: Double, remainingDose: Double): DoseSuggestion.ExactDose =
        DoseSuggestion.ExactDose(
            dosage = Dosage(
                amount = min(remainingDose, limit),
                unitOfMeasurement = DosageUnit.Milligram,
            )
        )
}

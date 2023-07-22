package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseSuggestion
import javax.inject.Inject
import kotlin.math.min

/**
 * Calculate the dosage range suggestion for a patient based on the given constraints.
 */
interface CalculateDoseRangeUseCase {

    /**
     * Run this use case.
     *
     * @param minimumAmount The minimum allowed dosage amount.
     * @param maximumAmount The maximum allowed dosage amount.
     * @param remainingDose The remaining dose that can be administered to the patient.
     * @return A [DoseSuggestion] object representing the dosage range suggestion.
     *         The suggestion can be either:
     *          - [DoseSuggestion.ExactDose]: If the remaining dose is less than or equal to the minimum amount.
     *            In this case, the suggestion will be a limited dosage amount determined by the CalculateLimitedDosageUseCase.
     *          - [DoseSuggestion.DoseRange]: If the remaining dose is greater than the minimum amount.
     *            In this case, the suggestion will be a range of dosages, with a minimum dosage of the minimumAmount
     *            and a maximum dosage calculated based on the remaining dose and maximum amount constraints.
     */
    operator fun invoke(
        minimumAmount: Double,
        maximumAmount: Double,
        remainingDose: Double,
    ): DoseSuggestion
}

class CalculateDoseRangeUseCaseImpl @Inject constructor(
    private val calculateLimitedDosageUseCase: CalculateLimitedDosageUseCase,
) : CalculateDoseRangeUseCase {

    override fun invoke(
        minimumAmount: Double,
        maximumAmount: Double,
        remainingDose: Double
    ): DoseSuggestion =
        if (remainingDose <= minimumAmount) {
            calculateLimitedDosageUseCase(minimumAmount, remainingDose)
        } else {
            DoseSuggestion.DoseRange(
                minimumDosage = Dosage(
                    amount = minimumAmount,
                    unitOfMeasurement = DosageUnit.Milligram,
                ),
                maximumDosage = Dosage(
                    amount = min(remainingDose, maximumAmount),
                    unitOfMeasurement = DosageUnit.Milligram,
                ),
            )
        }
}

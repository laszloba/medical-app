package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.Diagnosis
import com.vaslufi.medicalapp.domain.model.SeverityLevel
import javax.inject.Inject

/**
 * Check for severe conditions based on a set of diagnoses.
 */
interface CheckSevereConditionsUseCase {

    /**
     * Run this use case.
     *
     * @param diagnosis A set of diagnoses to check for severe conditions.
     * @return `true` if any of the diagnoses in the set indicate severe conditions, `false` otherwise.
     */
    operator fun invoke(diagnosis: Set<Diagnosis>): Boolean
}

class CheckSevereConditionsUseCaseImpl @Inject constructor() : CheckSevereConditionsUseCase {

    override fun invoke(diagnosis: Set<Diagnosis>): Boolean =
        diagnosis.contains(Diagnosis.Malnourishment(SeverityLevel.Severe)) ||
                diagnosis.contains(Diagnosis.Dehydration(SeverityLevel.Severe))

}

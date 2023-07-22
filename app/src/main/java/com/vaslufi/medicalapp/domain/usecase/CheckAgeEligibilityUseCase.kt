package com.vaslufi.medicalapp.domain.usecase

import javax.inject.Inject

/**
 * Check if the age is greater than or equal to 10 and
 * not within the range of 13 to 15 (both inclusive), to determine eligibility.
 */
interface CheckAgeEligibilityUseCase {

    /**
     * Run this use case.
     *
     * @param age The age to be checked for eligibility.
     * @return `true` if the age is eligible, `false` otherwise.
     */
    operator fun invoke(age: Int): Boolean
}

class CheckAgeEligibilityUseCaseImpl @Inject constructor() : CheckAgeEligibilityUseCase {

    override fun invoke(age: Int): Boolean = age >= 10 && age !in 13..15
}

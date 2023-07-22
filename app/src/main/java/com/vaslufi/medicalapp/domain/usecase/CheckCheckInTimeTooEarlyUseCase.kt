package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.DoseIntake
import org.joda.time.DateTime
import org.joda.time.Duration
import javax.inject.Inject

/**
 * Check if the check-in time is too early based on the medication intake history.
 */
interface CheckCheckInTimeTooEarlyUseCase {

    /**
     * Run this use case.
     *
     * @param checkInTime The check-in time to be checked.
     * @param history The list of [DoseIntake] representing the medication intake history.
     * @return `true` if the check-in time is too early, `false` otherwise.
     */
    operator fun invoke(
        checkInTime: DateTime,
        history: List<DoseIntake>,
    ): Boolean
}

class CheckCheckInTimeTooEarlyUseCaseImpl @Inject constructor() : CheckCheckInTimeTooEarlyUseCase {

    override operator fun invoke(
        checkInTime: DateTime,
        history: List<DoseIntake>,
    ): Boolean {
        if (history.isEmpty()) return false
        val lastDate = history.maxByOrNull { it.timeOfIntake }?.timeOfIntake
            ?: throw RuntimeException("There should be a maximum value if the history is not empty.")
        val duration = Duration(lastDate, checkInTime)
        val hoursDifference = duration.standardHours.toInt()
        return hoursDifference < CHECK_IN_INTERVAL_IN_HOURS
    }
}

private const val CHECK_IN_INTERVAL_IN_HOURS = 4

package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.usecase.FormatRemainingTimeUseCase.FormattedRemainingTime
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.PeriodFormatter
import javax.inject.Inject

/**
 * Format the remaining time until the next dose intake based on the given current time and dose intake history.
 */
interface FormatRemainingTimeUseCase {
    /**
     * Run this use case.
     *
     * @param currentTime The current date-time at which the remaining time should be calculated.
     * @param history The list of dose intake history used to determine the next intake time.
     * @return A [FormattedRemainingTime] object containing the formatted remaining time and the formatted next intake timestamp.
     *
     * @throws RuntimeException if the history is empty and no previous dose intake is available to calculate the next intake time.
     */
    operator fun invoke(
        currentTime: DateTime,
        history: List<DoseIntake>,
    ): FormattedRemainingTime

    data class FormattedRemainingTime(
        val remaining: String,
        val until: String,
    )
}

class FormatRemainingTimeUseCaseImpl @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter,
    private val periodFormatter: PeriodFormatter,
) : FormatRemainingTimeUseCase {

    override fun invoke(
        currentTime: DateTime,
        history: List<DoseIntake>,
    ): FormattedRemainingTime {
        history.maxByOrNull { it.timeOfIntake }?.let { latestDoseIntake ->
            val lastDate = latestDoseIntake.timeOfIntake
            val nextDate = lastDate.plusHours(CHECK_IN_INTERVAL_IN_HOURS)
            val duration = Duration(currentTime, nextDate)
            return FormattedRemainingTime(
                remaining = periodFormatter.print(duration.toPeriod()),
                until = dateTimeFormatter.print(nextDate),
            )
        }
        // TODO Improve error handling
        throw RuntimeException("The history is empty.")
    }
}

private const val CHECK_IN_INTERVAL_IN_HOURS = 4

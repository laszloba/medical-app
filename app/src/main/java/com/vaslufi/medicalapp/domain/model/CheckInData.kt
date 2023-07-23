package com.vaslufi.medicalapp.domain.model

import org.joda.time.DateTime

data class CheckInData(
    val time: DateTime,
    val temperature: Temperature,
    val feelingBetter: Boolean,
)

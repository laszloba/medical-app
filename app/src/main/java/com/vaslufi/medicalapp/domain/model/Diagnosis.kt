package com.vaslufi.medicalapp.domain.model

sealed class Diagnosis {
    data class Malnourishment(val severityLevel: SeverityLevel) : Diagnosis()
    data class Dehydration(val severityLevel: SeverityLevel) : Diagnosis()
}

package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.Temperature
import javax.inject.Inject

/**
 * Check if a given temperature is considered low.
 */
interface CheckLowTemperatureUseCase {

    /**
     * Run this use case.
     *
     * @param temperature The [Temperature] to be checked.
     * @return `true` if the temperature is considered low, `false` otherwise.
     */
    operator fun invoke(temperature: Temperature): Boolean
}

class CheckLowTemperatureUseCaseImpl @Inject constructor() : CheckLowTemperatureUseCase {

    override operator fun invoke(temperature: Temperature): Boolean =
        temperature.value <= LOW_TEMPERATURE_IN_CELSIUS
}

private const val LOW_TEMPERATURE_IN_CELSIUS = 38

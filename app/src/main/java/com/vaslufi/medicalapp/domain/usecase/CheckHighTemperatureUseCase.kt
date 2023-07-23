package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.domain.model.Temperature
import javax.inject.Inject

/**
 * Check if a given temperature is considered high.
 */
interface CheckHighTemperatureUseCase {

    /**
     * Run this use case.
     *
     * @param temperature The [Temperature] to be checked.
     * @return `true` if the temperature is considered high, `false` otherwise.
     */
    operator fun invoke(temperature: Temperature): Boolean
}

class CheckHighTemperatureUseCaseImpl @Inject constructor() : CheckHighTemperatureUseCase {

    override operator fun invoke(temperature: Temperature): Boolean =
        temperature.value >= HIGH_TEMPERATURE_IN_CELSIUS
}

private const val HIGH_TEMPERATURE_IN_CELSIUS = 40

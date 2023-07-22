package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.Temperature
import com.vaslufi.medicalapp.domain.model.TemperatureUnit
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CheckLowTemperatureUseCaseImplTest : MockKUnitTest() {

    @InjectMockKs
    lateinit var tested: CheckLowTemperatureUseCaseImpl

    @Test
    fun `Given a temperature exactly 38 Celsius, When invoke is called, Then it should return true`() {
        val borderlineHighTemperature = Temperature(38.0, TemperatureUnit.Celsius)

        val result = tested(borderlineHighTemperature)

        result.shouldBeTrue()
    }

    @Test
    fun `Given a temperature lower than 38 Celsius, When invoke is called, Then it should return true`() {
        val highTemperature = Temperature(37.5, TemperatureUnit.Celsius)

        val result = tested(highTemperature)

        result.shouldBeTrue()
    }

    @Test
    fun `Given a temperature higher than 38 Celsius, When invoke is called, Then it should return false`() {
        val normalTemperature = Temperature(38.2, TemperatureUnit.Celsius)

        val result = tested(normalTemperature)

        result.shouldBeFalse()
    }
}

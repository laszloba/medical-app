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
class CheckHighTemperatureUseCaseImplTest : MockKUnitTest() {

    @InjectMockKs
    lateinit var tested: CheckHighTemperatureUseCaseImpl

    @Test
    fun `Given a temperature exactly 40 Celsius, When invoke is called, Then it should return true`() {
        val borderlineHighTemperature = Temperature(40.0, TemperatureUnit.Celsius)

        val result = tested(borderlineHighTemperature)

        result.shouldBeTrue()
    }
    @Test
    fun `Given a temperature higher than 40 Celsius, When invoke is called, Then it should return true`() {
        val highTemperature = Temperature(40.5, TemperatureUnit.Celsius)

        val result = tested(highTemperature)

        result.shouldBeTrue()
    }

    @Test
    fun `Given a temperature lower than 40 Celsius, When invoke is called, Then it should return false`() {
        val normalTemperature = Temperature(37.2, TemperatureUnit.Celsius)

        val result = tested(normalTemperature)

        result.shouldBeFalse()
    }
}

package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseSuggestion
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CalculateDoseRangeUseCaseImplTest : MockKUnitTest() {

    @MockK
    lateinit var calculateLimitedDosageUseCase: CalculateLimitedDosageUseCaseImpl

    @InjectMockKs
    lateinit var tested: CalculateDoseRangeUseCaseImpl

    @Test
    fun `Given the remaining dose less than the minimum amount, When invoke is called, Then it should return a DoseSuggestion with ExactDose`() {
        val minimumAmount = 50.0
        val maximumAmount = 100.0
        val remainingDose = 30.0
        val limitedDosage = Dosage(amount = 30.0, unitOfMeasurement = DosageUnit.Milligram)
        every { calculateLimitedDosageUseCase(minimumAmount, remainingDose) } returns
                DoseSuggestion.ExactDose(limitedDosage)

        val result = tested(minimumAmount, maximumAmount, remainingDose)

        val expectedDosage = Dosage(amount = 30.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DoseSuggestion.ExactDose(expectedDosage)
        result shouldBe expectedDoseSuggestion
    }

    @Test
    fun `Given the remaining dose greater than the minimum amount, When invoke is called, Then it should return a DoseSuggestion with DoseRange`() {
        val minimumAmount = 50.0
        val maximumAmount = 100.0
        val remainingDose = 80.0

        val result = tested(minimumAmount, maximumAmount, remainingDose)

        val expectedMinDosage = Dosage(amount = 50.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedMaxDosage = Dosage(amount = 80.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DoseSuggestion.DoseRange(expectedMinDosage, expectedMaxDosage)
        result shouldBe expectedDoseSuggestion
        verify(exactly = 0) { calculateLimitedDosageUseCase.invoke(any(), any()) }
    }

    @Test
    fun `Given the remaining dose greater than the maximum amount, When invoke is called, Then it should return a DoseSuggestion with DoseRange limited by the maximum amount`() {
        val minimumAmount = 50.0
        val maximumAmount = 100.0
        val remainingDose = 120.0

        val result = tested(minimumAmount, maximumAmount, remainingDose)

        val expectedMinDosage = Dosage(amount = 50.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedMaxDosage = Dosage(amount = 100.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DoseSuggestion.DoseRange(expectedMinDosage, expectedMaxDosage)
        result shouldBe expectedDoseSuggestion
        verify(exactly = 0) { calculateLimitedDosageUseCase.invoke(any(), any()) }
    }
}

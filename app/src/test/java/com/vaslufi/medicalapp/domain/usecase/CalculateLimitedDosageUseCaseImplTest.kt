package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseSuggestion
import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CalculateLimitedDosageUseCaseImplTest : MockKUnitTest() {

    @InjectMockKs
    lateinit var tested: CalculateLimitedDosageUseCaseImpl

    @Test
    fun `Given a limit and remaining dose greater than the limit, When invoke is called, Then it should return an ExactDose object with the limit as dosage amount and Milligram as unit`() {
        val limit = 100.0
        val remainingDose = 150.0

        val result = tested(limit, remainingDose)

        val expectedDosage = Dosage(amount = limit, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DoseSuggestion.ExactDose(dosage = expectedDosage)
        result shouldBe expectedDoseSuggestion
    }

    @Test
    fun `Given a limit and remaining dose less than the limit, When invoke is called, Then it should return an ExactDose object with the remaining dose as dosage amount and Milligram as unit`() {
        val limit = 100.0
        val remainingDose = 75.0

        val result = tested(limit, remainingDose)

        val expectedDosage = Dosage(amount = remainingDose, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DoseSuggestion.ExactDose(dosage = expectedDosage)
        result shouldBe expectedDoseSuggestion
    }

    @Test
    fun `Given a limit and remaining dose equal to the limit, When invoke is called, Then it should return an ExactDose object with the limit as dosage amount and Milligram as unit`() {
        val limit = 100.0
        val remainingDose = 100.0

        val result = tested(limit, remainingDose)

        val expectedDosage = Dosage(amount = limit, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DoseSuggestion.ExactDose(dosage = expectedDosage)
        result shouldBe expectedDoseSuggestion
    }
}

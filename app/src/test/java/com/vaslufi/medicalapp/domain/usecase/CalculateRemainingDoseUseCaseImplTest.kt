package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.ApplicationMethod
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.Medication
import com.vaslufi.medicalapp.domain.model.MedicationAdministration
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CalculateRemainingDoseUseCaseImplTest : MockKUnitTest() {

    @MockK
    lateinit var calculateTotalDoseInLast24HoursUseCase: CalculateTotalDoseInLast24HoursUseCase

    @InjectMockKs
    lateinit var tested: CalculateRemainingDoseUseCaseImpl

    @Test
    fun `Given a history with total dosage in the last 24 hours less than the maximum dose, When invoke is called, Then it should return the difference as the remaining dosage`() {
        val currentTime = DateTime.now()
        val totalDoseInLast24Hours = 2000.0
        val history = listOf(
            DoseIntake(
                medicationAdministration = anyMedicationAdmin(),
                timeOfIntake = currentTime.minusHours(5)
            )
        )
        every {
            calculateTotalDoseInLast24HoursUseCase(currentTime, history)
        } returns totalDoseInLast24Hours

        val result = tested(currentTime, history)

        result shouldBe (4000.0 - totalDoseInLast24Hours)
    }

    @Test
    fun `Given a history with total dosage in the last 24 hours equal to the maximum dose, When invoke is called, Then it should return 0 as the remaining dosage`() {
        val currentTime = DateTime.now()
        val totalDoseInLast24Hours = 4000.0
        val history = listOf(
            DoseIntake(
                medicationAdministration = anyMedicationAdmin(),
                timeOfIntake = currentTime.minusHours(5)
            )
        )
        every {
            calculateTotalDoseInLast24HoursUseCase(currentTime, history)
        } returns totalDoseInLast24Hours

        val result = tested(currentTime, history)

        result shouldBe 0.0
    }

    @Test
    fun `Given a history with total dosage in the last 24 hours greater than the maximum dose, When invoke is called, Then it should return 0 as the remaining dosage`() {
        val currentTime = DateTime.now()
        val totalDoseInLast24Hours = 4500.0
        val history = listOf(
            DoseIntake(
                medicationAdministration = anyMedicationAdmin(),
                timeOfIntake = currentTime.minusHours(5)
            )
        )
        every {
            calculateTotalDoseInLast24HoursUseCase(currentTime, history)
        } returns totalDoseInLast24Hours

        val result = tested(currentTime, history)

        result shouldBe 0.0
    }

    private fun anyMedicationAdmin(): MedicationAdministration {
        return MedicationAdministration(
            medication = Medication.Paracetamol,
            dosage = Dosage(amount = 100.0, unitOfMeasurement = DosageUnit.Milligram),
            applicationMethod = ApplicationMethod.Oral
        )
    }
}

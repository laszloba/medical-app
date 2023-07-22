package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.ApplicationMethod
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.Medication
import com.vaslufi.medicalapp.domain.model.MedicationAdministration
import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CalculateTotalDoseInLast24HoursUseCaseImplTest : MockKUnitTest() {

    @InjectMockKs
    lateinit var tested: CalculateTotalDoseInLast24HoursUseCaseImpl

    @Test
    fun `Given the history of dose intakes with time within the last 24 hours, When invoke is called, Then it should return the total dosage intake in the last 24 hours`() {
        val currentTime = DateTime.now()
        val twentyFourHoursAgo = currentTime.minusHours(24)
        val history = listOf(
            createDoseIntake(twentyFourHoursAgo.plusMinutes(10), 50.0),
            createDoseIntake(twentyFourHoursAgo.plusHours(12), 25.0),
            createDoseIntake(twentyFourHoursAgo.plusHours(20), 30.0),
        )

        val result = tested(currentTime, history)

        result shouldBe 105.0
    }

    @Test
    fun `Given the history of dose intakes with time exactly 24 hours ago, When invoke is called, Then it should return the total dosage intake in the last 24 hours`() {
        val currentTime = DateTime.now()
        val twentyFourHoursAgo = currentTime.minusHours(24)
        val history = listOf(
            createDoseIntake(twentyFourHoursAgo, 50.0),
            createDoseIntake(twentyFourHoursAgo.plusHours(2), 25.0),
            createDoseIntake(twentyFourHoursAgo.plusHours(10), 30.0),
        )

        val result = tested(currentTime, history)

        result shouldBe 105.0
    }

    @Test
    fun `Given the history of dose intakes with all time before the last 24 hours, When invoke is called, Then it should return 0 as the total dosage intake in the last 24 hours`() {
        val currentTime = DateTime.now()
        val twentyFourHoursAgo = currentTime.minusHours(24)
        val history = listOf(
            createDoseIntake(twentyFourHoursAgo.minusMinutes(10), 50.0),
            createDoseIntake(twentyFourHoursAgo.minusHours(12), 25.0),
            createDoseIntake(twentyFourHoursAgo.minusHours(20), 30.0),
        )

        val result = tested(currentTime, history)

        result shouldBe 0.0
    }

    private fun createDoseIntake(timeOfIntake: DateTime, dosageAmount: Double): DoseIntake =
        DoseIntake(
            medicationAdministration = MedicationAdministration(
                medication = Medication.Paracetamol,
                dosage = Dosage(
                    amount = dosageAmount,
                    unitOfMeasurement = DosageUnit.Milligram,
                ),
                applicationMethod = ApplicationMethod.Oral,
            ),
            timeOfIntake = timeOfIntake,
        )
}

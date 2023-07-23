package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.ApplicationMethod
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.Medication
import com.vaslufi.medicalapp.domain.model.MedicationAdministration
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CheckCheckInTimeTooEarlyUseCaseImplTest : MockKUnitTest() {

    @InjectMockKs
    lateinit var tested: CheckCheckInTimeTooEarlyUseCaseImpl

    @Test
    fun `Given an empty medication intake history, When invoke is called, Then it should return false`() {
        val checkInTime = DateTime.now()
        val history = emptyList<DoseIntake>()

        val result = tested(checkInTime, history)

        result.shouldBeFalse()
    }

    @Test
    fun `Given a check-in time more than 4 hours after the last intake, When invoke is called, Then it should return false`() {
        val checkInTime = DateTime.now()
        val lastIntakeTime = checkInTime.minusHours(5)
        val history = listOf(createDoseIntake(lastIntakeTime))

        val result = tested(checkInTime, history)

        result.shouldBeFalse()
    }

    @Test
    fun `Given a check-in time exactly 4 hours after the last intake, When invoke is called, Then it should return false`() {
        val checkInTime = DateTime.now()
        val lastIntakeTime = checkInTime.minusHours(4)
        val history = listOf(createDoseIntake(lastIntakeTime))

        val result = tested(checkInTime, history)

        result.shouldBeFalse()
    }

    @Test
    fun `Given a check-in time less than 4 hours after the last intake, When invoke is called, Then it should return true`() {
        val checkInTime = DateTime.now()
        val lastIntakeTime = checkInTime.minusHours(3)
        val history = listOf(createDoseIntake(lastIntakeTime))

        val result = tested(checkInTime, history)

        result.shouldBeTrue()
    }

    private fun createDoseIntake(timeOfIntake: DateTime): DoseIntake =
        DoseIntake(
            medicationAdministration = MedicationAdministration(
                medication = Medication.Paracetamol,
                dosage = Dosage(
                    amount = 500.0,
                    unitOfMeasurement = DosageUnit.Milligram,
                ),
                applicationMethod = ApplicationMethod.Oral,
            ),
            timeOfIntake = timeOfIntake,
        )
}

package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.ApplicationMethod
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.Medication
import com.vaslufi.medicalapp.domain.model.MedicationAdministration
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.ReadableInstant
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.PeriodFormatter
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class FormatRemainingTimeUseCaseImplTest : MockKUnitTest() {

    @MockK
    lateinit var dateTimeFormatter: DateTimeFormatter

    @MockK
    lateinit var periodFormatter: PeriodFormatter

    @InjectMockKs
    lateinit var tested: FormatRemainingTimeUseCaseImpl

    @Test
    fun `Given non-empty history, When invoke is called, Then it should return formatted remaining time and next intake timestamp`() {
        val currentTime = DateTime.now()
        val history = listOf(
            createDoseIntake(currentTime.minusHours(2)),
            createDoseIntake(currentTime),
        )
        val nextDate = currentTime.plusHours(4)
        val duration = Duration(currentTime, nextDate)
        val formattedRemainingTime = "04:00"
        val formattedNextIntakeTime = "14:00:00 23/07/2023"
        every { periodFormatter.print(duration.toPeriod()) } returns formattedRemainingTime
        every { dateTimeFormatter.print(nextDate) } returns formattedNextIntakeTime

        val result = tested(currentTime, history)

        result.remaining shouldBe formattedRemainingTime
        result.until shouldBe formattedNextIntakeTime
        verify { dateTimeFormatter.print(nextDate) }
        verify { periodFormatter.print(duration.toPeriod()) }
    }

    @Test
    fun `Given empty history, When invoke is called, Then it should throw a RuntimeException`() {
        val currentTime = DateTime.now()
        val history = emptyList<DoseIntake>()

        shouldThrow<RuntimeException> {
            tested(currentTime, history)
        }

        verify(exactly = 0) { dateTimeFormatter.print(any<ReadableInstant>()) }
        verify(exactly = 0) { periodFormatter.print(any()) }
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

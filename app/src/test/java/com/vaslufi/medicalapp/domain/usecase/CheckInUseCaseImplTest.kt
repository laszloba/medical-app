package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.Advice
import com.vaslufi.medicalapp.domain.model.ApplicationMethod
import com.vaslufi.medicalapp.domain.model.CheckInData
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageCalculationResult
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.DoseSuggestion
import com.vaslufi.medicalapp.domain.model.Medication
import com.vaslufi.medicalapp.domain.model.MedicationAdministration
import com.vaslufi.medicalapp.domain.model.Patient
import com.vaslufi.medicalapp.domain.model.Temperature
import com.vaslufi.medicalapp.domain.model.TemperatureUnit
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CheckInUseCaseImplTest : MockKUnitTest() {

    @MockK
    lateinit var checkLowTemperatureUseCase: CheckLowTemperatureUseCase

    @MockK
    lateinit var checkHighTemperatureUseCase: CheckHighTemperatureUseCase

    @MockK
    lateinit var checkCheckInTimeTooEarlyUseCase: CheckCheckInTimeTooEarlyUseCase

    @MockK
    lateinit var calculateDosageUseCase: CalculateDosageUseCase

    @InjectMockKs
    lateinit var tested: CheckInUseCaseImpl

    @Test
    fun `Given high temperature, When invoke is called, Then it should return SeekMedicalAttention advice`() {
        val patient = Patient(name = "Patient", age = 30, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val checkInData = CheckInData(
            time = currentTime,
            temperature = Temperature(value = 40.5, unit = TemperatureUnit.Celsius),
            feelingBetter = false,
        )
        every { checkHighTemperatureUseCase(checkInData.temperature) } returns true
        every { checkLowTemperatureUseCase(checkInData.temperature) } returns false

        val advice = tested(patient, history, checkInData, currentTime)

        advice shouldBe Advice.SeekMedicalAttention
        verify(exactly = 0) {
            checkCheckInTimeTooEarlyUseCase.invoke(any(), any())
        }
        verify(exactly = 0) {
            calculateDosageUseCase.invoke(any(), any(), any())
        }
    }

    @Test
    fun `Given low temperature, When invoke is called, Then it should return NoTreatmentNeeded advice`() {
        val patient = Patient(name = "Patient", age = 30, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val checkInData = CheckInData(
            time = currentTime,
            temperature = Temperature(value = 37.0, unit = TemperatureUnit.Celsius),
            feelingBetter = false,
        )
        every { checkHighTemperatureUseCase(checkInData.temperature) } returns false
        every { checkLowTemperatureUseCase(checkInData.temperature) } returns true

        val advice = tested(patient, history, checkInData, currentTime)

        advice shouldBe Advice.NoTreatmentNeeded
        verify(exactly = 0) {
            checkCheckInTimeTooEarlyUseCase.invoke(any(), any())
        }
        verify(exactly = 0) {
            calculateDosageUseCase.invoke(any(), any(), any())
        }
    }

    @Test
    fun `Given feeling better, When invoke is called, Then it should return StopTreatment advice`() {
        val patient = Patient(name = "Patient", age = 25, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val checkInData = CheckInData(
            time = currentTime,
            temperature = Temperature(value = 37.2, unit = TemperatureUnit.Celsius),
            feelingBetter = true,
        )
        every { checkHighTemperatureUseCase(checkInData.temperature) } returns false
        every { checkLowTemperatureUseCase(checkInData.temperature) } returns false

        val advice = tested(patient, history, checkInData, currentTime)

        advice shouldBe Advice.StopTreatment
        verify(exactly = 0) {
            calculateDosageUseCase.invoke(any(), any(), any())
        }
    }

    @Test
    fun `Given too early check-in, When invoke is called, Then it should return TooEarlyCheckIn advice`() {
        val patient = Patient(name = "Patient", age = 18, diagnosis = emptySet())
        val currentTime = DateTime.now()
        val history = listOf(
            DoseIntake(
                medicationAdministration = MedicationAdministration(
                    medication = Medication.Paracetamol,
                    dosage = Dosage(amount = 500.0, unitOfMeasurement = DosageUnit.Milligram),
                    applicationMethod = ApplicationMethod.Oral,
                ),
                timeOfIntake = currentTime.minusHours(2),
            )
        )
        val checkInData = CheckInData(
            time = currentTime,
            temperature = Temperature(value = 38.0, unit = TemperatureUnit.Celsius),
            feelingBetter = false,
        )
        every { checkHighTemperatureUseCase(checkInData.temperature) } returns false
        every { checkLowTemperatureUseCase(checkInData.temperature) } returns false
        every { checkCheckInTimeTooEarlyUseCase(checkInData.time, history) } returns true

        val advice = tested(patient, history, checkInData, currentTime)

        advice shouldBe Advice.TooEarlyCheckIn
        verify(exactly = 0) {
            calculateDosageUseCase.invoke(any(), any(), any())
        }
    }

    @Test
    fun `Given dosage calculation result with AgeNotSupported, When invoke is called, Then it should return SeekMedicalAttention advice`() {
        val patient = Patient(name = "Patient", age = 3, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val checkInData = CheckInData(
            time = currentTime,
            temperature = Temperature(value = 36.5, unit = TemperatureUnit.Celsius),
            feelingBetter = false
        )
        every { checkHighTemperatureUseCase(checkInData.temperature) } returns false
        every { checkLowTemperatureUseCase(checkInData.temperature) } returns false
        every { checkCheckInTimeTooEarlyUseCase.invoke(currentTime, history) } returns false
        every {
            calculateDosageUseCase(patient, history, currentTime)
        } returns DosageCalculationResult.AgeNotSupported

        val advice = tested(patient, history, checkInData, currentTime)

        advice shouldBe Advice.SeekMedicalAttention
    }

    @Test
    fun `Given dosage calculation result with MaxLimitReached, When invoke is called, Then it should return MaxLimitReached advice`() {
        val patient = Patient(name = "Patient", age = 30, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val checkInData = CheckInData(
            time = currentTime,
            temperature = Temperature(value = 37.0, unit = TemperatureUnit.Celsius),
            feelingBetter = false
        )
        every { checkHighTemperatureUseCase(checkInData.temperature) } returns false
        every { checkLowTemperatureUseCase(checkInData.temperature) } returns false
        every { checkCheckInTimeTooEarlyUseCase(checkInData.time, history) } returns false
        every {
            calculateDosageUseCase(patient, history, currentTime)
        } returns DosageCalculationResult.MaxLimitReached

        val advice = tested(patient, history, checkInData, currentTime)

        advice shouldBe Advice.MaxLimitReached
    }

    @Test
    fun `Given dosage calculation result with TakeDose, When invoke is called, Then it should return TakeDose advice`() {
        val patient = Patient(name = "Patient", age = 20, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val checkInData = CheckInData(
            time = currentTime,
            temperature = Temperature(value = 36.9, unit = TemperatureUnit.Celsius),
            feelingBetter = false
        )
        val doseSuggestion = DoseSuggestion.ExactDose(
            Dosage(
                amount = 500.0,
                unitOfMeasurement = DosageUnit.Milligram,
            )
        )
        every { checkHighTemperatureUseCase(checkInData.temperature) } returns false
        every { checkLowTemperatureUseCase(checkInData.temperature) } returns false
        every { checkCheckInTimeTooEarlyUseCase(checkInData.time, history) } returns false
        every {
            calculateDosageUseCase(patient, history, currentTime)
        } returns DosageCalculationResult.TakeDose(doseSuggestion)

        val advice = tested(patient, history, checkInData, currentTime)

        advice shouldBe Advice.TakeDose(doseSuggestion)
    }
}

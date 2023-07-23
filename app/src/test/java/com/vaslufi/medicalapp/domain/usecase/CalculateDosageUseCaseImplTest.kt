package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.Diagnosis
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageCalculationResult
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.DoseSuggestion
import com.vaslufi.medicalapp.domain.model.Patient
import com.vaslufi.medicalapp.domain.model.SeverityLevel
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CalculateDosageUseCaseImplTest : MockKUnitTest() {

    @MockK
    lateinit var calculateRemainingDoseUseCase: CalculateRemainingDoseUseCase

    @MockK
    lateinit var checkAgeEligibilityUseCase: CheckAgeEligibilityUseCase

    @MockK
    lateinit var checkSevereConditionsUseCase: CheckSevereConditionsUseCase

    @MockK
    lateinit var calculateLimitedDosageUseCase: CalculateLimitedDosageUseCase

    @MockK
    lateinit var calculateDoseRangeUseCase: CalculateDoseRangeUseCase

    @InjectMockKs
    lateinit var tested: CalculateDosageUseCaseImpl

    @Test
    fun `Given the patient's age is not supported, When invoke is called, Then it should return AgeNotSupported`() {
        val patient = Patient(name = "Patient", age = 8, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        every { checkAgeEligibilityUseCase(patient.age) } returns false

        val result = tested(patient, history, currentTime)

        result shouldBe DosageCalculationResult.AgeNotSupported
    }

    @Test
    fun `Given the patient's remaining dose is 0, When invoke is called, Then it should return MaxLimitReached`() {
        val patient = Patient(name = "Patient", age = 12, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        every { checkAgeEligibilityUseCase(patient.age) } returns true
        every { calculateRemainingDoseUseCase(currentTime, history) } returns 0.0

        val result = tested(patient, history, currentTime)

        result shouldBe DosageCalculationResult.MaxLimitReached
    }

    @Test
    fun `Given the patient is 10 years old and has no severe conditions, When invoke is called, Then it should calculate a limited dosage`() {
        val patient = Patient(name = "Patient", age = 10, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val remainingDose = 2000.0
        every { checkAgeEligibilityUseCase(patient.age) } returns true
        every { checkSevereConditionsUseCase(patient.diagnosis) } returns true
        every { calculateRemainingDoseUseCase(currentTime, history) } returns remainingDose
        every {
            calculateLimitedDosageUseCase(limit = 500.0, remainingDose = remainingDose)
        } returns DoseSuggestion.ExactDose(
            Dosage(
                amount = 500.0,
                unitOfMeasurement = DosageUnit.Milligram,
            )
        )

        val result = tested(patient, history, currentTime)

        val expectedDosage = Dosage(amount = 500.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion =
            DosageCalculationResult.TakeDose(DoseSuggestion.ExactDose(expectedDosage))
        result shouldBe expectedDoseSuggestion
    }

    @Test
    fun `Given the patient is 10 years old and has severe conditions, When invoke is called, Then it should calculate a limited dosage`() {
        val patient = Patient(
            name = "Patient",
            age = 10,
            diagnosis = setOf(
                Diagnosis.Malnourishment(SeverityLevel.Severe),
            ),
        )
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val remainingDose = 2000.0
        every { checkAgeEligibilityUseCase(patient.age) } returns true
        every { checkSevereConditionsUseCase(patient.diagnosis) } returns true
        every { calculateRemainingDoseUseCase(currentTime, history) } returns remainingDose
        every {
            calculateLimitedDosageUseCase(limit = 500.0, remainingDose = remainingDose)
        } returns DoseSuggestion.ExactDose(
            Dosage(
                amount = 500.0,
                unitOfMeasurement = DosageUnit.Milligram,
            )
        )

        val result = tested(patient, history, currentTime)

        val expectedDosage = Dosage(amount = 500.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion =
            DosageCalculationResult.TakeDose(DoseSuggestion.ExactDose(expectedDosage))
        result shouldBe expectedDoseSuggestion
    }

    @Test
    fun `Given the patient is 12 years old, When invoke is called, Then it should calculate a limited dosage`() {
        val patient = Patient(name = "Patient", age = 12, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val remainingDose = 3000.0
        every { checkAgeEligibilityUseCase(patient.age) } returns true
        every { checkSevereConditionsUseCase(patient.diagnosis) } returns false
        every { calculateRemainingDoseUseCase(currentTime, history) } returns remainingDose
        every {
            calculateLimitedDosageUseCase(limit = 625.0, remainingDose = remainingDose)
        } returns DoseSuggestion.ExactDose(
            Dosage(
                amount = 625.0,
                unitOfMeasurement = DosageUnit.Milligram,
            )
        )

        val result = tested(patient, history, currentTime)

        val expectedDosage = Dosage(amount = 625.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion =
            DosageCalculationResult.TakeDose(DoseSuggestion.ExactDose(expectedDosage))
        result shouldBe expectedDoseSuggestion
    }

    @Test
    fun `Given the patient is 18 years old, When invoke is called, Then it should calculate a dosage range with the maximum is the remaining amount`() {
        val patient = Patient(name = "Patient", age = 18, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val remainingDose = 800.0
        every { checkAgeEligibilityUseCase(patient.age) } returns true
        every { checkSevereConditionsUseCase(patient.diagnosis) } returns false
        every { calculateRemainingDoseUseCase(currentTime, history) } returns remainingDose
        every {
            calculateDoseRangeUseCase(
                minimumAmount = 500.0,
                maximumAmount = 1000.0,
                remainingDose = remainingDose,
            )
        } returns DoseSuggestion.DoseRange(
            minimumDosage = Dosage(amount = 500.0, unitOfMeasurement = DosageUnit.Milligram),
            maximumDosage = Dosage(amount = 800.0, unitOfMeasurement = DosageUnit.Milligram),
        )

        val result = tested(patient, history, currentTime)

        val expectedMinDosage = Dosage(amount = 500.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedMaxDosage = Dosage(amount = 800.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DosageCalculationResult.TakeDose(
            DoseSuggestion.DoseRange(
                minimumDosage = expectedMinDosage,
                maximumDosage = expectedMaxDosage,
            )
        )
        result shouldBe expectedDoseSuggestion
    }

    @Test
    fun `Given the patient is 18 years old, When invoke is called, Then it should calculate a dosage range with the maximum is the maximum limit`() {
        val patient = Patient(name = "Patient", age = 18, diagnosis = emptySet())
        val history = emptyList<DoseIntake>()
        val currentTime = DateTime.now()
        val remainingDose = 1200.0
        every { checkAgeEligibilityUseCase(patient.age) } returns true
        every { checkSevereConditionsUseCase(patient.diagnosis) } returns false
        every { calculateRemainingDoseUseCase(currentTime, history) } returns remainingDose
        every {
            calculateDoseRangeUseCase(
                minimumAmount = 500.0,
                maximumAmount = 1000.0,
                remainingDose = remainingDose,
            )
        } returns DoseSuggestion.DoseRange(
                minimumDosage = Dosage(amount = 500.0, unitOfMeasurement = DosageUnit.Milligram),
                maximumDosage = Dosage(amount = 1000.0, unitOfMeasurement = DosageUnit.Milligram),
            )

        val result = tested(patient, history, currentTime)

        val expectedMinDosage = Dosage(amount = 500.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedMaxDosage = Dosage(amount = 1000.0, unitOfMeasurement = DosageUnit.Milligram)
        val expectedDoseSuggestion = DosageCalculationResult.TakeDose(
            DoseSuggestion.DoseRange(
                minimumDosage = expectedMinDosage,
                maximumDosage = expectedMaxDosage
            )
        )
        result shouldBe expectedDoseSuggestion
    }
}

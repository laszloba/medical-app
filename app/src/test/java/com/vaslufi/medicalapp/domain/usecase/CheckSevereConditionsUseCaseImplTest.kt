package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import com.vaslufi.medicalapp.domain.model.Diagnosis
import com.vaslufi.medicalapp.domain.model.SeverityLevel
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CheckSevereConditionsUseCaseImplTest : MockKUnitTest() {

    @InjectMockKs
    lateinit var tested: CheckSevereConditionsUseCaseImpl

    @Test
    fun `Given malnourishment with severe severity level, When invoke is called, Then it should return true`() {
        val diagnoses = setOf(
            Diagnosis.Malnourishment(SeverityLevel.Severe),
        )

        val result = tested(diagnoses)

        result.shouldBeTrue()
    }

    @Test
    fun `Given dehydration with severe severity level, When invoke is called, Then it should return true`() {
        val diagnoses = setOf(
            Diagnosis.Dehydration(SeverityLevel.Severe),
        )

        val result = tested(diagnoses)

        result.shouldBeTrue()
    }

    @Test
    fun `Given no diagnosis, When invoke is called, Then it should return false`() {
        val diagnoses = emptySet<Diagnosis>()

        val result = tested(diagnoses)

        result.shouldBeFalse()
    }

    @Test
    fun `Given no diagnosis with severe severity level, When invoke is called, Then it should return false`() {
        val diagnoses = setOf(
            Diagnosis.Malnourishment(SeverityLevel.Mild),
            Diagnosis.Malnourishment(SeverityLevel.Moderate),
            Diagnosis.Dehydration(SeverityLevel.Mild),
            Diagnosis.Dehydration(SeverityLevel.Moderate),
        )

        val result = tested(diagnoses)

        result.shouldBeFalse()
    }
}

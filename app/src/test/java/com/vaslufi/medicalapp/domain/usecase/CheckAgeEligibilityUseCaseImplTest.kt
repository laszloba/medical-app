package com.vaslufi.medicalapp.domain.usecase

import com.vaslufi.medicalapp.MockKUnitTest
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CheckAgeEligibilityUseCaseImplTest : MockKUnitTest() {

    @InjectMockKs
    lateinit var tested: CheckAgeEligibilityUseCaseImpl

    @Test
    fun `Given an age greater than or equal to 10 and not within the range of 13 to 15 (both inclusive), When invoke is called, Then it should return true`() {
        val eligibleAge = 16

        val result = tested(eligibleAge)

        result.shouldBeTrue()
    }

    @Test
    fun `Given an age equal to 10, When invoke is called, Then it should return true`() {
        val age = 10

        val result = tested(age)

        result.shouldBeTrue()
    }

    @Test
    fun `Given an age within the range of 13 to 15 (both inclusive), When invoke is called, Then it should return false`() {
        val ineligibleAge = 14

        val result = tested(ineligibleAge)

        result.shouldBeFalse()
    }

    @Test
    fun `Given an age lower than 10, When invoke is called, Then it should return false`() {
        val ineligibleAge = 9

        val result = tested(ineligibleAge)

        result.shouldBeFalse()
    }
}

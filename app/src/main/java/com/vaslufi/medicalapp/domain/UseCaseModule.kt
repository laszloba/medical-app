package com.vaslufi.medicalapp.domain

import com.vaslufi.medicalapp.domain.usecase.CalculateDosageUseCase
import com.vaslufi.medicalapp.domain.usecase.CalculateDosageUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CalculateDoseRangeUseCase
import com.vaslufi.medicalapp.domain.usecase.CalculateDoseRangeUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CalculateLimitedDosageUseCase
import com.vaslufi.medicalapp.domain.usecase.CalculateLimitedDosageUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CalculateRemainingDoseUseCase
import com.vaslufi.medicalapp.domain.usecase.CalculateRemainingDoseUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CalculateTotalDoseInLast24HoursUseCase
import com.vaslufi.medicalapp.domain.usecase.CalculateTotalDoseInLast24HoursUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckAgeEligibilityUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckAgeEligibilityUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckCheckInTimeTooEarlyUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckCheckInTimeTooEarlyUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckHighTemperatureUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckHighTemperatureUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckInUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckInUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckLowTemperatureUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckLowTemperatureUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckSevereConditionsUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckSevereConditionsUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.FormatRemainingTimeUseCase
import com.vaslufi.medicalapp.domain.usecase.FormatRemainingTimeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindCalculateDosageUseCase(implementation: CalculateDosageUseCaseImpl): CalculateDosageUseCase

    @Binds
    abstract fun bindCheckInUseCase(implementation: CheckInUseCaseImpl): CheckInUseCase

    @Binds
    abstract fun bindCheckCheckInTimeTooEarlyUseCase(implementation: CheckCheckInTimeTooEarlyUseCaseImpl): CheckCheckInTimeTooEarlyUseCase

    @Binds
    abstract fun bindCheckLowTemperatureUseCase(implementation: CheckLowTemperatureUseCaseImpl): CheckLowTemperatureUseCase

    @Binds
    abstract fun bindCheckHighTemperatureUseCase(implementation: CheckHighTemperatureUseCaseImpl): CheckHighTemperatureUseCase

    @Binds
    abstract fun bindCalculateTotalDoseInLast24HoursUseCase(implementation: CalculateTotalDoseInLast24HoursUseCaseImpl): CalculateTotalDoseInLast24HoursUseCase

    @Binds
    abstract fun bindCalculateRemainingDoseUseCase(implementation: CalculateRemainingDoseUseCaseImpl): CalculateRemainingDoseUseCase

    @Binds
    abstract fun bindCheckAgeEligibilityUseCase(implementation: CheckAgeEligibilityUseCaseImpl): CheckAgeEligibilityUseCase

    @Binds
    abstract fun bindCheckSevereConditionsUseCase(implementation: CheckSevereConditionsUseCaseImpl): CheckSevereConditionsUseCase

    @Binds
    abstract fun bindCalculateLimitedDosageUseCase(implementation: CalculateLimitedDosageUseCaseImpl): CalculateLimitedDosageUseCase

    @Binds
    abstract fun bindCalculateDoseRangeUseCase(implementation: CalculateDoseRangeUseCaseImpl): CalculateDoseRangeUseCase

    @Binds
    abstract fun bindFormatRemainingTimeUseCase(implementation: FormatRemainingTimeUseCaseImpl): FormatRemainingTimeUseCase
}

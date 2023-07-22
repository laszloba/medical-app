package com.vaslufi.medicalapp.domain

import com.vaslufi.medicalapp.domain.usecase.CalculateTotalDoseInLast24HoursUseCase
import com.vaslufi.medicalapp.domain.usecase.CalculateTotalDoseInLast24HoursUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckAgeEligibilityUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckAgeEligibilityUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckCheckInTimeTooEarlyUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckCheckInTimeTooEarlyUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckHighTemperatureUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckHighTemperatureUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckLowTemperatureUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckLowTemperatureUseCaseImpl
import com.vaslufi.medicalapp.domain.usecase.CheckSevereConditionsUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckSevereConditionsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindCheckCheckInTimeTooEarlyUseCase(implementation: CheckCheckInTimeTooEarlyUseCaseImpl): CheckCheckInTimeTooEarlyUseCase

    @Binds
    abstract fun bindCheckLowTemperatureUseCase(implementation: CheckLowTemperatureUseCaseImpl): CheckLowTemperatureUseCase

    @Binds
    abstract fun bindCheckHighTemperatureUseCase(implementation: CheckHighTemperatureUseCaseImpl): CheckHighTemperatureUseCase

    @Binds
    abstract fun bindCalculateTotalDoseInLast24HoursUseCase(implementation: CalculateTotalDoseInLast24HoursUseCaseImpl): CalculateTotalDoseInLast24HoursUseCase

    @Binds
    abstract fun bindCheckAgeEligibilityUseCase(implementation: CheckAgeEligibilityUseCaseImpl): CheckAgeEligibilityUseCase

    @Binds
    abstract fun bindCheckSevereConditionsUseCase(implementation: CheckSevereConditionsUseCaseImpl): CheckSevereConditionsUseCase
}

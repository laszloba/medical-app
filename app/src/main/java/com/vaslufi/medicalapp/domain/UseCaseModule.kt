package com.vaslufi.medicalapp.domain

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
    abstract fun bindCheckLowTemperatureUseCase(implementation: CheckLowTemperatureUseCaseImpl): CheckLowTemperatureUseCase

    @Binds
    abstract fun bindCheckHighTemperatureUseCase(implementation: CheckHighTemperatureUseCaseImpl): CheckHighTemperatureUseCase

    @Binds
    abstract fun bindCheckSevereConditionsUseCase(implementation: CheckSevereConditionsUseCaseImpl): CheckSevereConditionsUseCase
}

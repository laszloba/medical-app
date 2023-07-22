package com.vaslufi.medicalapp.domain

import com.vaslufi.medicalapp.domain.usecase.CheckHighTemperatureUseCase
import com.vaslufi.medicalapp.domain.usecase.CheckHighTemperatureUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindCheckHighTemperatureUseCase(implementation: CheckHighTemperatureUseCaseImpl): CheckHighTemperatureUseCase
}

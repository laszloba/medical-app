package com.vaslufi.medicalapp.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

@Module
@InstallIn(SingletonComponent::class)
object DateTimeFormatterModule {

    @Provides
    fun provideDateTimeFormatter(): DateTimeFormatter =
        DateTimeFormat.forPattern("HH:mm:ss dd/MM/yyyy")

    @Provides
    fun providePeriodFormatter(): PeriodFormatter =
        PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendLiteral(":")
            .appendMinutes()
            .toFormatter()
}

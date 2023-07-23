package com.vaslufi.medicalapp.ui.home.impl

import androidx.lifecycle.ViewModel
import com.vaslufi.medicalapp.domain.model.Advice
import com.vaslufi.medicalapp.domain.model.ApplicationMethod
import com.vaslufi.medicalapp.domain.model.CheckInData
import com.vaslufi.medicalapp.domain.model.Dosage
import com.vaslufi.medicalapp.domain.model.DosageUnit
import com.vaslufi.medicalapp.domain.model.DoseIntake
import com.vaslufi.medicalapp.domain.model.DoseSuggestion
import com.vaslufi.medicalapp.domain.model.DoseSuggestion.DoseRange
import com.vaslufi.medicalapp.domain.model.DoseSuggestion.ExactDose
import com.vaslufi.medicalapp.domain.model.Medication
import com.vaslufi.medicalapp.domain.model.MedicationAdministration
import com.vaslufi.medicalapp.domain.model.Patient
import com.vaslufi.medicalapp.domain.model.Temperature
import com.vaslufi.medicalapp.domain.model.TemperatureUnit
import com.vaslufi.medicalapp.domain.usecase.CheckInUseCase
import com.vaslufi.medicalapp.domain.usecase.FormatRemainingTimeUseCase
import com.vaslufi.medicalapp.ui.home.DebugInformation
import com.vaslufi.medicalapp.ui.home.HomeState
import com.vaslufi.medicalapp.ui.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * This view model is for demonstration purposes only.
 * It needs to be refactored to enhance testability and separate concerns.
 * Suggested changes include but not limited to:
 *  - Implementing a repository module for storing medication intake history and managing patient data
 *  - Adding a use case for time formatting
 *  - Removing debug information
 */
// TODO Refactor
@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val checkInUseCase: CheckInUseCase,
    private val formatRemainingTimeUseCase: FormatRemainingTimeUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel(), HomeViewModel {
    override val state = MutableStateFlow<HomeState>(HomeState.Start)
    override val debugInfo = MutableStateFlow(DebugInformation.EMPTY)

    private val history = mutableListOf<DoseIntake>()

    private var patient = DEFAULT_PATIENT
    private var virtualTime = DateTime.now()

    init {
        debugReset()
    }

    override fun checkIn(temperature: Double, feelingBetter: Boolean) {
        val result = checkInUseCase.invoke(
            patient = patient,
            history = history,
            checkInData = CheckInData(
                time = virtualTime,
                temperature = Temperature(
                    value = temperature,
                    unit = TemperatureUnit.Celsius,
                ),
                feelingBetter = feelingBetter,
            ),
            currentTime = virtualTime,
        )
        state.value = when (result) {
            Advice.MaxLimitReached -> HomeState.MaxLimitReached
            Advice.SeekMedicalAttention -> HomeState.SeekMedicalAttention
            Advice.StopTreatment -> HomeState.StopTreatment
            Advice.NoTreatmentNeeded -> HomeState.NoTreatmentNeeded
            is Advice.TakeDose -> takeDose(result.doseSuggestion)
            Advice.TooEarlyCheckIn -> tooEarlyCheckIn()
        }
    }

    // TODO Refactor the dose formatter into a use case and implement localization (remove hardcoded "mg" value)
    private fun takeDose(doseSuggestion: DoseSuggestion) =
        when (doseSuggestion) {
            is ExactDose ->
                HomeState.TakeDose(
                    minimumAmount = doseSuggestion.dosage.amount,
                    maximumAmount = doseSuggestion.dosage.amount,
                    formattedDose = "${doseSuggestion.dosage.amount.toInt()}mg",
                )

            is DoseRange -> HomeState.TakeDose(
                minimumAmount = doseSuggestion.minimumDosage.amount,
                maximumAmount = doseSuggestion.maximumDosage.amount,
                formattedDose = "${doseSuggestion.minimumDosage.amount.toInt()}mg" +
                        " - ${doseSuggestion.maximumDosage.amount.toInt()}mg",
            )
        }

    private fun tooEarlyCheckIn(): HomeState.TooEarlyCheckIn {
        val formattedRemainingTime = formatRemainingTimeUseCase(virtualTime, history)
        return HomeState.TooEarlyCheckIn(
            formattedRemainingTime = formattedRemainingTime.remaining,
            formattedUntilTime = formattedRemainingTime.until,
        )
    }

    override fun recordDosage(amount: Double) {
        history.add(
            DoseIntake(
                medicationAdministration = MedicationAdministration(
                    medication = Medication.Paracetamol,
                    dosage = Dosage(
                        amount = amount,
                        unitOfMeasurement = DosageUnit.Milligram,
                    ),
                    applicationMethod = ApplicationMethod.Oral,
                ),
                timeOfIntake = virtualTime,
            )
        )
        val formattedRemainingTime = formatRemainingTimeUseCase(virtualTime, history)
        state.value = HomeState.DosageRecorded(
            formattedRemainingTime = formattedRemainingTime.remaining,
            formattedUntilTime = formattedRemainingTime.until,
        )
        updateDebugInfo()
    }

    override fun debugReset() {
        patient = DEFAULT_PATIENT
        history.clear()
        virtualTime = DateTime.now()
        state.value = HomeState.Start
        updateDebugInfo()
    }

    override fun debugAdvanceTimeBy(minutes: Int) {
        virtualTime = virtualTime.plusMinutes(minutes)
        updateDebugInfo()
    }

    override fun debugSetPatientAge(age: Int) {
        patient = patient.copy(age = age)
        updateDebugInfo()
    }

    private fun updateDebugInfo() {
        debugInfo.value = DebugInformation(
            virtualTime = dateTimeFormatter.print(virtualTime),
            history = history.joinToString("\n") { doseIntake ->
                val amount = doseIntake.medicationAdministration.dosage.amount.toString()
                val formattedDate = dateTimeFormatter.print(doseIntake.timeOfIntake)
                "${amount}mg at $formattedDate"
            },
            patient = patient,
        )
    }

    companion object {
        private val DEFAULT_PATIENT = Patient(
            name = "Patient",
            age = 40,
            diagnosis = emptySet(),
        )
    }
}

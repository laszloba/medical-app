package com.vaslufi.medicalapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaslufi.medicalapp.R
import com.vaslufi.medicalapp.ui.home.HomeState.DosageRecorded
import com.vaslufi.medicalapp.ui.home.HomeState.MaxLimitReached
import com.vaslufi.medicalapp.ui.home.HomeState.NoTreatmentNeeded
import com.vaslufi.medicalapp.ui.home.HomeState.SeekMedicalAttention
import com.vaslufi.medicalapp.ui.home.HomeState.Start
import com.vaslufi.medicalapp.ui.home.HomeState.StopTreatment
import com.vaslufi.medicalapp.ui.home.HomeState.TakeDose
import com.vaslufi.medicalapp.ui.home.HomeState.TooEarlyCheckIn
import com.vaslufi.medicalapp.ui.theme.MedicalAppTheme

@Composable
fun Home(
    viewModel: HomeViewModel,
) {
    val state by viewModel.state.collectAsState()
    val debugInfo by viewModel.debugInfo.collectAsState()
    Home(
        state = state,
        debugInfo = debugInfo,
        onCheckIn = viewModel::checkIn,
        onRecordClick = viewModel::recordDosage,
        onAdvanceTimeByClick = viewModel::debugAdvanceTimeBy,
        onResetClick = viewModel::debugReset,
        onSetPatientAgeClick = viewModel::debugSetPatientAge,
    )
}

@Composable
private fun Home(
    state: HomeState,
    debugInfo: DebugInformation,
    onCheckIn: (Double, Boolean) -> Unit,
    onRecordClick: (Double) -> Unit,
    onAdvanceTimeByClick: (Int) -> Unit,
    onResetClick: () -> Unit,
    onSetPatientAgeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var debugCardVisibility by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    Row(modifier = Modifier.clickable {
                        debugCardVisibility = !debugCardVisibility
                    }) {
                        Switch(
                            checked = debugCardVisibility,
                            onCheckedChange = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Debug mode")
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (state) {
                Start -> StartMessage()
                SeekMedicalAttention -> SeekMedicalAttentionMessage()
                MaxLimitReached -> MaxLimitReachedMessage()
                StopTreatment -> StopTreatmentMessage()
                NoTreatmentNeeded -> NoTreatmentNeededMessage()
                is TooEarlyCheckIn ->
                    TooEarlyCheckInMessage(
                        formattedRemainingTime = state.formattedRemainingTime,
                        formattedUntilTime = state.formattedUntilTime,
                    )

                is DosageRecorded ->
                    DosageRecordedMessage(
                        formattedRemainingTime = state.formattedRemainingTime,
                        formattedUntilTime = state.formattedUntilTime,
                    )

                is TakeDose -> {
                    TakeDoseMessage(state.formattedDose)
                    DosageRecordCard(
                        minimumAmount = state.minimumAmount,
                        maximumAmount = state.maximumAmount,
                        onRecordClick = onRecordClick,
                    )
                }
            }
            HealthCheckCard(onCheckInClick = onCheckIn)
            if (debugCardVisibility) {
                Spacer(modifier = Modifier.height(16.dp))
                DebugCard(
                    onAdvanceTimeByClick = onAdvanceTimeByClick,
                    onResetClick = onResetClick,
                    onSetPatientAgeClick = onSetPatientAgeClick,
                    patientAge = debugInfo.patient.age.toString(),
                    time = debugInfo.virtualTime,
                    history = debugInfo.history,
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    MedicalAppTheme {
        Home(
            state = Start,
            debugInfo = DebugInformation.EMPTY,
            onCheckIn = { _, _ -> },
            onRecordClick = {},
            onAdvanceTimeByClick = { },
            onResetClick = { },
            onSetPatientAgeClick = { },
        )
    }
}

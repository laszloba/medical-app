package com.vaslufi.medicalapp.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.vaslufi.medicalapp.R
import com.vaslufi.medicalapp.ui.theme.CustomColor

@Preview
@Composable
fun StartMessage() {
    InfoCard(
        icon = Icons.Outlined.Thermostat,
        iconTint = CustomColor.Cyan,
        message = stringResource(R.string.home_message_start),
    )
}

@Preview
@Composable
fun SeekMedicalAttentionMessage() {
    InfoCard(
        icon = Icons.Default.Emergency,
        iconTint = CustomColor.White,
        message = stringResource(R.string.home_message_seek_medical_attention),
        urgent = true,
    )
}

@Preview
@Composable
fun MaxLimitReachedMessage() {
    InfoCard(
        icon = Icons.Default.FrontHand,
        iconTint = CustomColor.Red,
        message = stringResource(R.string.home_message_max_limit_reached),
    )
}

@Preview
@Composable
fun StopTreatmentMessage() {
    InfoCard(
        icon = Icons.Default.CheckCircle,
        iconTint = CustomColor.Green,
        message = stringResource(R.string.home_message_stop_treatment),
    )
}

@Preview
@Composable
fun NoTreatmentNeededMessage() {
    InfoCard(
        icon = Icons.Default.CheckCircle,
        iconTint = CustomColor.Cyan,
        message = stringResource(R.string.home_message_no_treatment_needed),
    )
}

@Composable
fun TooEarlyCheckInMessage(
    formattedRemainingTime: String,
    formattedUntilTime: String,
) {
    InfoCard(
        icon = Icons.Outlined.Schedule,
        iconTint = CustomColor.Gray,
        message = stringResource(
            R.string.home_message_too_early_check_in, formattedRemainingTime, formattedUntilTime
        ),
    )
}

@Preview
@Composable
private fun TooEarlyCheckInMessagePreview() {
    TooEarlyCheckInMessage(
        formattedRemainingTime = "02:00",
        formattedUntilTime = "14:00:00 23/07/2023",
    )
}

@Composable
fun DosageRecordedMessage(
    formattedRemainingTime: String,
    formattedUntilTime: String,
) {
    InfoCard(
        icon = Icons.Outlined.Medication,
        iconTint = CustomColor.Green,
        message = stringResource(
            R.string.home_message_dosage_recorded, formattedRemainingTime, formattedUntilTime
        ),
    )
}

@Preview
@Composable
private fun DosageRecordedMessagePreview() {
    DosageRecordedMessage(
        formattedRemainingTime = "04:00",
        formattedUntilTime = "14:00:00 23/07/2023",
    )
}

@Composable
fun TakeDoseMessage(formattedDose: String) {
    InfoCard(
        icon = Icons.Outlined.AddCircleOutline,
        iconTint = CustomColor.Green,
        message = stringResource(R.string.home_message_take_dose_message, formattedDose),
    )
}

@Preview
@Composable
private fun TakeDoseMessagePreview() {
    TakeDoseMessage(
        formattedDose = "500mg",
    )
}

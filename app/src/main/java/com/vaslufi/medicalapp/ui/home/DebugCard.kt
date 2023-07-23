package com.vaslufi.medicalapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaslufi.medicalapp.R
import com.vaslufi.medicalapp.ui.theme.MedicalAppTheme

@Composable
fun DebugCard(
    onAdvanceTimeByClick: (Int) -> Unit,
    onResetClick: () -> Unit,
    onSetPatientAgeClick: (Int) -> Unit,
    patientAge: String,
    time: String,
    history: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.debug_card_title),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(R.string.debug_card_virtual_time_label, time))
            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(R.string.debug_card_advance_time_by_label))
            Row {
                OutlinedButton(
                    onClick = { onAdvanceTimeByClick(30) }) {
                    Text(stringResource(R.string.debug_card_advance_by_30m))
                }
                OutlinedButton(
                    onClick = { onAdvanceTimeByClick(60) },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text(stringResource(R.string.debug_card_advance_by_1h)) }
                OutlinedButton(
                    onClick = { onAdvanceTimeByClick(120) },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text(stringResource(R.string.debug_card_advance_by_2h)) }
                OutlinedButton(
                    onClick = { onAdvanceTimeByClick(240) },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text(stringResource(R.string.debug_card_advance_by_4h)) }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(R.string.debug_card_patients_age_label, patientAge))
            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(R.string.debug_card_set_patients_age_label))
            Row {
                OutlinedButton(onClick = { onSetPatientAgeClick(10) }) {
                    Text(stringResource(R.string.debug_card_set_patients_age_to_10))
                }
                OutlinedButton(
                    onClick = { onSetPatientAgeClick(12) },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text(stringResource(R.string.debug_card_set_patients_age_to_12)) }
                OutlinedButton(
                    onClick = { onSetPatientAgeClick(40) },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text(stringResource(R.string.debug_card_set_patients_age_to_40)) }
            }
            if (history.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.debug_card_history_label),
                    style = MaterialTheme.typography.h6,
                )
                Text(history)
            }
            OutlinedButton(onClick = onResetClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                )
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.debug_card_reset_button))
            }
        }
    }
}

@Preview
@Composable
private fun DebugCardPreviewWithoutHistory() {
    MedicalAppTheme {
        DebugCard(
            onAdvanceTimeByClick = {},
            onResetClick = {},
            onSetPatientAgeClick = {},
            patientAge = "40",
            time = "14:00:00 23/07/2023",
            history = "",
        )
    }
}

@Preview
@Composable
private fun DebugCardPreviewWithHistory() {
    MedicalAppTheme {
        DebugCard(
            onAdvanceTimeByClick = {},
            onResetClick = {},
            onSetPatientAgeClick = {},
            patientAge = "40",
            time = "14:00:00 23/07/2023",
            history = "500mg at 14:00:00 23/07/2023\n" + "1000mg at 18:00:00 23/07/2023\n" + "1000mg at 22:00:00 23/07/2023",
        )
    }
}

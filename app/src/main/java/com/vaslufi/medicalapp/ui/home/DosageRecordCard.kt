package com.vaslufi.medicalapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaslufi.medicalapp.R
import com.vaslufi.medicalapp.ui.theme.MedicalAppTheme

@Composable
fun DosageRecordCard(
    onRecordClick: (Double) -> Unit,
    minimumAmount: Double,
    maximumAmount: Double,
    modifier: Modifier = Modifier,
) {
    var dosage by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val invalidInputMessage = stringResource(R.string.common_error_invalid_input)
    val lessThanMinimumAmountMessage =
        stringResource(R.string.dosage_record_error_less_than_minimum_amount, minimumAmount)
    val moreThanMaximumAmountMessage =
        stringResource(R.string.dosage_record_error_more_than_maximum_amount, maximumAmount)
    val exactAmountNeededMessage =
        stringResource(R.string.dosage_record_error_exact_amount_needed, minimumAmount)

    val isValid: (String) -> Boolean = { input ->
        val num = input.toDoubleOrNull()
        when {
            num == null && input.isNotEmpty() -> {
                error = invalidInputMessage
                false
            }

            minimumAmount == maximumAmount && num != null && num != minimumAmount -> {
                error = exactAmountNeededMessage
                false
            }

            num != null && num < minimumAmount -> {
                error = lessThanMinimumAmountMessage
                false
            }

            num != null && num > maximumAmount -> {
                error = moreThanMaximumAmountMessage
                false
            }

            else -> {
                error = null
                true
            }
        }
    }

    Card(
        modifier = modifier
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.dosage_record_instruction_label),
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = dosage,
                    onValueChange = {
                        dosage = it
                        isValid(it)
                    },
                    placeholder = { Text(stringResource(R.string.dosage_record_instruction_placeholder)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = error != null,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.dosage_record_dosage_unit),
                    style = MaterialTheme.typography.body1,
                )
            }
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.body2.copy(color = Color.Red),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Button(
                onClick = {
                    error = null
                    onRecordClick(dosage.toDoubleOrNull() ?: 0.0)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = dosage.toDoubleOrNull() != null && error == null,
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = stringResource(R.string.dosage_record_record_button_icon_content_description),
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.dosage_record_record_button))
            }
        }
    }
}

@Preview
@Composable
private fun DosageRecordCardPreview() {
    MedicalAppTheme {
        DosageRecordCard(
            onRecordClick = { },
            minimumAmount = 500.0,
            maximumAmount = 1000.0,
        )
    }
}

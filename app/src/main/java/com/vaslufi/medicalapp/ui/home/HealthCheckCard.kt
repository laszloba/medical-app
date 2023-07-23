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
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaslufi.medicalapp.R
import com.vaslufi.medicalapp.ui.theme.MedicalAppTheme

@Composable
fun HealthCheckCard(
    onCheckInClick: (Double, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    var temperature by remember { mutableStateOf("") }
    var feelingBetter by remember { mutableStateOf(false) }
    var radioOption by remember { mutableStateOf(RadioOption.NoneSelected) }
    var error by remember { mutableStateOf<String?>(null) }

    val invalidInputMessage = stringResource(R.string.common_error_invalid_input)

    val isValid: (String) -> Boolean = { input ->
        val num = input.toDoubleOrNull()

        when {
            num == null && input.isNotEmpty() -> {
                error = invalidInputMessage
                false
            }

            else -> {
                error = null
                true
            }
        }
    }

    Card(
        modifier = modifier.padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.check_in_question),
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = radioOption == RadioOption.Yes,
                    onClick = { radioOption = RadioOption.Yes; feelingBetter = true })
                Text(stringResource(R.string.check_in_answer_yes))
                RadioButton(selected = radioOption == RadioOption.No,
                    onClick = { radioOption = RadioOption.No; feelingBetter = false })
                Text(stringResource(R.string.check_in_answer_no))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.check_in_temperature_label))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = temperature,
                onValueChange = {
                    temperature = it
                    isValid(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.check_in_temperature_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = error != null,
            )

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.body2.copy(color = Color.Red),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            val checkInButtonEnabled =
                feelingBetter || (temperature.toDoubleOrNull() != null && error == null && radioOption != RadioOption.NoneSelected)
            Button(
                onClick = {
                    error = null
                    onCheckInClick(temperature.toDoubleOrNull() ?: 0.0, feelingBetter)
                    temperature = ""
                    radioOption = RadioOption.NoneSelected
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = checkInButtonEnabled,
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = stringResource(R.string.check_in_check_in_button_icon_content_description),
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.check_in_check_in_button))
            }
        }
    }
}

private enum class RadioOption {
    NoneSelected, Yes, No,
}

@Preview
@Composable
private fun HealthCheckCardPreview() {
    MedicalAppTheme {
        HealthCheckCard(
            onCheckInClick = { _, _ -> },
        )
    }
}

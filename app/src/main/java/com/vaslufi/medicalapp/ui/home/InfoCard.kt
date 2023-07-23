package com.vaslufi.medicalapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sick
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaslufi.medicalapp.ui.theme.CustomColor
import com.vaslufi.medicalapp.ui.theme.MedicalAppTheme

@Composable
fun InfoCard(
    icon: ImageVector,
    iconTint: Color,
    message: String,
    modifier: Modifier = Modifier,
    urgent: Boolean = false,
) {
    val backgroundColor = if (urgent) CustomColor.Red else MaterialTheme.colors.surface
    val textColor = if (urgent) CustomColor.White else MaterialTheme.colors.onSurface

    Card(
        modifier = modifier
            .padding(16.dp),
        elevation = 4.dp,
        backgroundColor = backgroundColor,
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(backgroundColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Icon",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(80.dp),
                tint = iconTint,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.body1.copy(color = textColor),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun InfoCardPreview() {
    MedicalAppTheme {
        InfoCard(
            icon = Icons.Outlined.Sick,
            iconTint = CustomColor.Pink,
            message = "If you feel that you might have a high temperature, please measure it.",
        )
    }
}

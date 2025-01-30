package com.example.gains.ui.screens.nutrition

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.gains.features.nutrition.NutritionViewModel
import com.example.gains.features.nutrition.Util.formatDate

@Composable
fun DateNav(viewModel: NutritionViewModel) {
    val forwardEnabled by viewModel.forwardEnabled.collectAsState()
    val date by viewModel.date.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { viewModel.changeDate(-1) }) {
            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = "Back Date"
            )
        }
        Text(
            text = "Viewing Logs for ${formatDate(date)}",
            modifier = Modifier
                .weight(2f),
            textAlign = TextAlign.Center,
        )
        IconButton(
            onClick = { viewModel.changeDate(1) },
            enabled = forwardEnabled
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = "Back Date"
            )
        }
    }

}
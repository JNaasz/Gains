package com.example.gains.ui.screens.nutrition

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.gains.features.nutrition.NutritionViewModel

@Composable
fun DateNav(viewModel: NutritionViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
            contentDescription = "date_back"
        )
        Text(
            text = "Viewing Logs for %DATE%",
            modifier = Modifier
                .weight(2f),
            textAlign = TextAlign.Center,
        )
        Icon(
            Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "date_forward"
        )

    }

}
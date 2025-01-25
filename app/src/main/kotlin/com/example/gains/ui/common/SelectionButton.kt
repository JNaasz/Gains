package com.example.gains.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Button - filled
// FilledTonalButton
// OutlinedButtonExample
// ElevatedButtonExample

@Composable
fun SelectionButton(label: String, action: () -> Unit, enabled: Boolean, type: String? = null) {
    if (type === "tonal") {
        FilledTonalButton(
            onClick = { action() },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(label)
        }
    } else {
        Button(
            onClick = { action() },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(label)
        }
    }
}


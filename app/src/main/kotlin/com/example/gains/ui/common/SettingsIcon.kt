package com.example.gains.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun SettingsIcon(
    navToSettings: () -> Unit,
) {
    IconButton(onClick = navToSettings) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "Settings Icon"
        )
    }
}

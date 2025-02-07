package com.example.gains.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun EditIcon(
    editAction: () -> Unit,
) {
    IconButton(onClick = { editAction() }) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit Icon"
        )
    }
}

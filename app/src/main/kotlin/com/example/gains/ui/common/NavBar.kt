package com.example.gains.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    title: String,
    scrollContent: @Composable (PaddingValues) -> Unit,
    optionalNavigationComponent: (@Composable () -> Unit)? = null,
    optionalActionComponent: (@Composable () -> Unit)? = null,
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    title, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                optionalNavigationComponent?.invoke()
            },
            actions = {
                optionalActionComponent?.invoke()
            }

        )
    }) { paddingValues ->
        scrollContent(paddingValues)
    }
}
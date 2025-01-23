package com.example.gains.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gains.database.NutritionLog
import com.example.gains.features.nutrition.NutritionViewModel
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp

@Composable
fun NutritionScreen(
    popBackStack: () -> Unit,
    navToLogNutrition: () -> Unit,
) {
    NavBar(
        title = "Protein Log",
        scrollContent = { paddingValues: PaddingValues -> NutritionContent(paddingValues, navToLogNutrition) },
        optionalActionComponent = {
            NavBackIcon(popBackStack)
        }
    )
}

@Composable
fun NutritionContent(
    paddingValues: PaddingValues,
    navToLogNutrition: () -> Unit
) {
    val viewModel: NutritionViewModel = hiltViewModel()
    val logs by viewModel.nutritionLogs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // LazyColumn for displaying the logs
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Ensures LazyColumn shares available space
                .fillMaxWidth()
        ) {
            items(logs) { log ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ProteinLog(log)
                    Button(
                        onClick = { viewModel.deleteLog(log) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }

        // Empty state message if no logs are present
        if (logs.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("No items logged yet today.")
            }
        }

        // Add Item button at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = navToLogNutrition) {
                Text("Add Item")
            }
        }
    }
}

@Composable
fun ProteinLog(log: NutritionLog) {
    Text("${log.foodName} | ${log.size}${log.unit} | ${log.protein}g protein")
}
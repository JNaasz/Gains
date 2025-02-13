package com.example.gains.ui.screens.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gains.database.NutritionLog
import com.example.gains.features.nutrition.NutritionViewModel
import com.example.gains.features.nutrition.SizeUnit
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar
import com.example.gains.ui.common.SelectionButton

@Composable
fun NutritionScreen(
    popBackStack: () -> Unit,
    navToLogNutrition: () -> Unit,
) {
    val viewModel: NutritionViewModel = hiltViewModel()
    val currentProteinTotal by viewModel.currentProteinTotal.collectAsState()

    NavBar(
        title = "Total Logged: ${currentProteinTotal.toInt()}g",
        scrollContent = { paddingValues: PaddingValues ->
            NutritionContent(
                paddingValues,
                navToLogNutrition,
                viewModel
            )
        },
        optionalActionComponent = {
            NavBackIcon(popBackStack)
        }
    )
}

@Composable
fun NutritionContent(
    paddingValues: PaddingValues,
    navToLogNutrition: () -> Unit,
    viewModel: NutritionViewModel
) {
    val logs by viewModel.nutritionLogs.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        DateNav(viewModel)
        // LazyColumn for displaying the logs

        if (logs.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Ensures LazyColumn shares available space
                    .fillMaxWidth()
            ) {
                items(logs) { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ProteinLog(
                            log,
                            modifier = Modifier.weight(2f)
                        )
                        Button(
                            onClick = { viewModel.deleteLog(log) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete")
                        }
                    }
                    HorizontalDivider()
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("No items logged.")
            }
        }

        // Add Item button at the bottom
        Row(
            modifier = Modifier.padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
        ) {
            SelectionButton(
                label = "Add Item",
                action = { navToLogNutrition() },
                enabled = true
            )
        }
    }
}

@Composable
fun ProteinLog(log: NutritionLog, modifier: Modifier) {
    val sizeText = if (log.unit == SizeUnit.SERVING.symbol) {
        "Servings: ${log.size}"
    } else {
        "${log.size}${log.unit}"
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = log.foodName
        )
        Text(
            text = sizeText
        )
        Text(
            text = "${log.protein}g protein"
        )

    }
}
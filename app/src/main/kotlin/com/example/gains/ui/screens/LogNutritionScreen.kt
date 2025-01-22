package com.example.gains.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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

@Composable
fun LogNutritionScreen(
    popBackStack: () -> Unit,
) {
    NavBar(
        title = "Log Protein",
        scrollContent = { paddingValues: PaddingValues -> Content(paddingValues) },
        optionalActionComponent = {
            NavBackIcon(popBackStack)
        }
    )
}

@Composable
fun Content(paddingValues: PaddingValues) {
    val viewModel: NutritionViewModel = hiltViewModel()
    val logs by viewModel.nutritionLogs.collectAsState()

    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        LazyColumn {
            items(logs) { log ->
                ProteinLog(log)
                Button(onClick = { viewModel.deleteLog(log) }) {
                    Text("Delete")
                }
            }
        }

        Row {
            Button(onClick = {  }) {
                Text("Add Item")
            }
        }
    }
}




@Composable
fun ProteinLog(log: NutritionLog) {
    Text("${log.foodName} | ${log.size}${log.unit} | ${ log.protein }g protein")
}
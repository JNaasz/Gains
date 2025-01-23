package com.example.gains.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gains.features.nutrition.LogNutritionViewModel
import com.example.gains.ui.common.DropdownSelector
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar

@Composable
fun LogNutritionScreen(popBackStack: () -> Unit) {
    NavBar(
        title = "Add New Protein Log",
        scrollContent = { paddingValues: PaddingValues -> LogNutritionContent(paddingValues) },
        optionalActionComponent = {
            NavBackIcon(popBackStack)
        }
    )
}

@Composable
fun LogNutritionContent(paddingValues: PaddingValues) {
    val viewModel: LogNutritionViewModel = hiltViewModel()

    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
        ) {
            QuantityInput(viewModel, "Quantity:")
            // float input
            DropdownSelector(
                "Unit:",
                options = viewModel.sizeUnits,
                setValue = viewModel::selectUnit
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Select form list OR add custom item:")
            // float input
        }
    }
}

@Composable
fun QuantityInput(viewModel: LogNutritionViewModel, label: String) {
    var input = viewModel.quantityInput.toString()
    TextField(
        value = input,
        onValueChange = { newVal ->
            input = newVal
            viewModel.quantityInput = newVal.toFloatOrNull() ?: 0f
        },
        label = { Text("Quantity:") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}
package com.example.gains.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        ContentRow(content = {
            // Quantity input
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp) // Add padding for spacing
            ) {
                QuantityInput(viewModel, "Quantity:")
            }

            // Dropdown selector
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp) // Add padding for spacing
            ) {
                DropdownSelector(
                    label = "Unit:",
                    options = viewModel.sizeUnits,
                    setValue = viewModel::selectUnit,
                )
            }
        })

        ContentRow(content = { Text("Select source form list:") })

        ContentRow(content = {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
            ) {
                DropdownSelector(
                    label = "Source:",
                    options = viewModel.sourceList,
                    setValue = viewModel::selectFoodType,
                )
            }
        })

        ContentRow(content = { Text("OR add custom item:") })

        // custom inputs
        ContentRow(content = {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
            ) {
                CustomSourceInput(viewModel, "Source:")
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
            ) {
                CustomContentInput(viewModel, "Grams / Unit:")
            }

            // button to add item - disabled until details are filled out
            // button to add custom item - disabled until details are filled out

            // when user adds custom item, prompt to see if they want to add to selection list
        })
    }
}

@Composable
fun ContentRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        content()
    }
}

@Composable
fun QuantityInput(viewModel: LogNutritionViewModel, label: String) {
    var input by remember { mutableStateOf(viewModel.quantityInput.toString()) }
    TextField(
        value = input,
        onValueChange = { newVal ->
            input = newVal
            viewModel.quantityInput = newVal.toFloatOrNull() ?: 0f
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}

@Composable
fun CustomSourceInput(viewModel: LogNutritionViewModel, label: String) {
    var input by remember { mutableStateOf("") }
    TextField(
        value = input,
        onValueChange = { newVal ->
            input = newVal
            viewModel.selectFoodType(newVal)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}


@Composable
fun CustomContentInput(viewModel: LogNutritionViewModel, label: String) {
    var input by remember { mutableStateOf("0") }
    TextField(
        value = input,
        onValueChange = { newVal ->
            input = newVal
            viewModel.customProteinContent = newVal.toFloatOrNull() ?: 0f
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}
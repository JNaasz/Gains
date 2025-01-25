package com.example.gains.ui.screens.logNutrition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gains.features.nutrition.LogNutritionViewModel
import com.example.gains.ui.common.DropdownSelector
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar
import com.example.gains.ui.common.SelectionButton

@Composable
fun LogNutritionScreen(popBackStack: () -> Unit) {
    NavBar(
        title = "Add New Protein Log",
        scrollContent = { paddingValues: PaddingValues -> LogNutritionContent(paddingValues, popBackStack) },
        optionalActionComponent = {
            NavBackIcon(popBackStack)
        }
    )
}

@Composable
fun LogNutritionContent(paddingValues: PaddingValues, popBackStack: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val viewModel: LogNutritionViewModel = hiltViewModel()
    val paddingModifier = Modifier.padding(8.dp)
    val customButtonEnabled by viewModel.customButtonEnabled.collectAsState()
    val selectionButtonEnabled by viewModel.selectionButtonEnabled.collectAsState()
    val showStoreDialog by viewModel.showDialog.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .clickable(
                indication = null, // Removes the ripple effect
                interactionSource = remember { MutableInteractionSource() } // Disables interaction tracking
            ) { focusManager.clearFocus() } // close keyboards on click
    ) {
        ContentRow(content = {
            Column(
                modifier = paddingModifier.weight(2f)
            ) {
                QuantityInput(viewModel, "Quantity:")
            }

            Column(
                modifier = paddingModifier.weight(2f)
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
                modifier = paddingModifier.weight(2f)
            ) {
                DropdownSelector(
                    label = "Source:",
                    options = viewModel.sourceList,
                    setValue = viewModel::selectFoodType,
                )
            }
        })

        ContentRow(content = {
            Box(
                modifier = Modifier.padding(start = 25.dp, end = 25.dp)
            ) {
                SelectionButton(
                    label = "Add Selection",
                    action = { viewModel.addSelectedItem() },
                    enabled = selectionButtonEnabled
                )
            }
        })

        ContentRow(content = { Text("OR add custom item:") })

        // custom inputs
        ContentRow(content = {
            Column(
                modifier = paddingModifier.weight(2f)
            ) {
                CustomSourceInput(viewModel, "Source:")
            }

            Column(
                modifier = paddingModifier.weight(2f)
            ) {
                CustomContentInput(viewModel, "Grams / Unit:")
            }
        })

        // custom submit
        ContentRow(content = {
            // when user adds custom item, prompt to see if they want to add to selection list
            Box(
                modifier = Modifier.padding(start = 25.dp, end = 25.dp)
            ) {
                SelectionButton(
                    label = "Add Custom Item",
                    action = { viewModel.addCustomItem() },
                    enabled = customButtonEnabled
                )
            }
        })

        // store custom dialog
        if (showStoreDialog) {
            ConfirmDialog(viewModel, popBackStack)
        }
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
    var input by remember { mutableStateOf("0") }
    TextField(
        value = input,
        onValueChange = { newVal ->
            input = newVal
            viewModel.setQuantityInput(newVal)
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
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    )
}

@Composable
fun CustomContentInput(viewModel: LogNutritionViewModel, label: String) {
    var input by remember { mutableStateOf("0") }
    TextField(
        value = input,
        onValueChange = { newVal ->
            input = newVal
            viewModel.setCustomProteinContent(newVal)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}

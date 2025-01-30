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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gains.features.nutrition.LogNutritionViewModel
import com.example.gains.features.nutrition.Util.formatDate
import com.example.gains.ui.common.DropdownSelector
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar
import com.example.gains.ui.common.SelectionButton

@Composable
fun LogNutritionScreen(popBackStack: () -> Unit) {
    NavBar(
        title = "Log Protein",
        scrollContent = { paddingValues: PaddingValues ->
            LogNutritionContent(
                paddingValues,
                popBackStack
            )
        },
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
    val addCustomItem by viewModel.addCustomItem.collectAsState()
    val customButtonEnabled by viewModel.customButtonEnabled.collectAsState()
    val selectionButtonEnabled by viewModel.selectionButtonEnabled.collectAsState()
    val showStoreDialog by viewModel.showDialog.collectAsState()
    val sourceData by viewModel.sourceData.collectAsState()
    val sourceList by viewModel.sourceList.collectAsState()
    val localContext = LocalContext.current

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
            Text("Logging Protein for ${formatDate(viewModel.selectedDate)}")
        })

        ContentRow(content = {
            Column(
                modifier = paddingModifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LaunchedEffect(Unit) { // Ensures fetchSourceList is called once when the composable is displayed
                    viewModel.fetchSourceList(localContext)
                }

                if (sourceList.isNotEmpty()) {
                    DropdownSelector(
                        label = "Source:",
                        options = sourceList,
                        setValue = viewModel::setSourceSelection,
                    )
                } else {
                    Text("Loading Sources..")
                }

                if (sourceData != null) {
                    Text(
                        text = "*${sourceData!!.servingSize}${sourceData!!.servingUnit}" +
                                " contains ${sourceData!!.proteinPerServing}g protein",
                        fontSize = 12.sp
                    )
                }
            }
        })

        ContentRow(content = {
            Column(
                modifier = paddingModifier.weight(2f)
            ) {
                QuantityInput(
                    label = "Quantity:",
                    setValue = { newValue ->
                        viewModel.setLogQuantity(newValue)
                    })
            }

            // TODO: If the selected source has a servingUnit of Serving, only allow Serving selection
            Column(
                modifier = paddingModifier.weight(2f)
            ) {
                DropdownSelector(
                    label = "Unit:",
                    options = viewModel.sizeUnitSelections,
                    setValue = viewModel::setLogUnit,
                )
            }
        })

        if (!addCustomItem) {
            ContentRow(content = {
                Box(
                    modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                ) {
                    SelectionButton(
                        label = "Add Selection",
                        action = {
                            viewModel.addLogFromSelection()
                            popBackStack()
                        },
                        enabled = selectionButtonEnabled
                    )
                }
            })
        } else {
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
                    QuantityInput(
                        label = "Grams Protein / Serving:",
                        setValue = { newValue ->
                            viewModel.setCustomProteinContent(newValue)
                        })
                }
            })

            StoreCustomOption(viewModel)

            // custom submit
            ContentRow(content = {
                Box(
                    modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                ) {
                    SelectionButton(
                        label = "Add Custom Item",
                        action = {
                            viewModel.addCustomItem()
                            if (!viewModel.storeCustom) {
                                popBackStack()
                            }
                        },
                        enabled = customButtonEnabled
                    )
                }
            })
        }

        // store custom dialog - TODO: not working
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun QuantityInput(label: String, setValue: (String) -> Unit) {
    var input by remember { mutableStateOf("0") }
    TextField(
        value = input,
        onValueChange = { newVal ->
            input = newVal.trimStart { it == '0' }
            setValue(newVal)
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
            viewModel.setCustomFoodName(newVal)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    )
}

@Composable
fun StoreCustomOption(viewModel: LogNutritionViewModel) {
    var checked by remember { mutableStateOf(true) }
    ContentRow(content = {
        Text("Remember Custom Item")
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                viewModel.storeCustom = it
            }
        )
    })
}

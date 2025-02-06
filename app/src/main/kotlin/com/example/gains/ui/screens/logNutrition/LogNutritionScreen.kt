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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.gains.features.nutrition.SizeUnit
import com.example.gains.features.nutrition.Util.CUSTOM
import com.example.gains.features.nutrition.Util.formatDate
import com.example.gains.features.nutrition.Util.localDateToEpochMilli
import com.example.gains.ui.common.DatePickerModal
import com.example.gains.ui.common.DropdownSelector
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar
import com.example.gains.ui.common.SelectionButton

@Composable
fun LogNutritionScreen(popBackStack: () -> Unit) {
    NavBar(title = "Log Protein", scrollContent = { paddingValues: PaddingValues ->
        LogNutritionContent(
            paddingValues, popBackStack
        )
    }, optionalActionComponent = {
        NavBackIcon(popBackStack)
    })
}

@Composable
fun LogNutritionContent(paddingValues: PaddingValues, popBackStack: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val viewModel: LogNutritionViewModel = hiltViewModel()
    val localContext = LocalContext.current
    val paddingModifier = Modifier.padding(8.dp)

    val sourceData by viewModel.sourceData.collectAsState()
    val sourceList by viewModel.sourceList.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val newCustomSource by viewModel.newSource.collectAsState()

    var storeCustom by remember { mutableStateOf(true) }
    var showDateDialog by remember { mutableStateOf(false) }
    var addCustomItem by remember { mutableStateOf(true) } // may just use source to check

    // keep track of the inputs
    var quantityInput by remember { mutableStateOf("0") }
    var unitInput by remember { mutableStateOf(SizeUnit.SERVING.symbol) }
    var sourceInput by remember { mutableStateOf("") }
    var proteinInput by remember { mutableStateOf("0") }

    var customButtonEnabled by remember { mutableStateOf(false) }
    var selectionButtonEnabled by remember { mutableStateOf(false) }

    // update button enablement states when input values change
    LaunchedEffect(quantityInput, unitInput, sourceInput, proteinInput, sourceData) {
        val quantity = quantityInput.toFloatOrNull() ?: 0F
        val protein = proteinInput.toFloatOrNull() ?: 0F

        customButtonEnabled = quantity > 0F && protein > 0F && sourceInput.isNotEmpty()
        selectionButtonEnabled = quantity > 0F && sourceData != null
    }

    fun addCustomLog() {
        viewModel.addCustomLog(sourceInput, unitInput, quantityInput, proteinInput)
    }

    Column(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .clickable(indication = null, // Removes the ripple effect
            interactionSource = remember { MutableInteractionSource() } // Disables interaction tracking
        ) { focusManager.clearFocus() } // close keyboards on click
    ) {

        ContentRow(content = {
            Text("Logging Protein for ${formatDate(selectedDate)}")
            IconButton(onClick = {
                showDateDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange, contentDescription = "Select Date"
                )
            }
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
                        setValue = {
                            viewModel.setSourceSelection(it)
                            addCustomItem = it == CUSTOM
                        },
                    )
                } else {
                    Text("Loading Sources..")
                }

                if (sourceData != null) {
                    Text(
                        text = "*${sourceData!!.servingSize}${sourceData!!.servingUnit}" + " contains ${sourceData!!.proteinPerServing}g protein",
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
                    value = quantityInput,
                    setValue = { newValue ->
                        quantityInput = newValue
                    }
                )
            }

            // TODO: If the selected source has a servingUnit of Serving, only allow Serving selection
            Column(
                modifier = paddingModifier.weight(2f)
            ) {
                DropdownSelector(
                    label = "Unit:",
                    options = viewModel.sizeUnitSelections,
                    setValue = { value ->
                        unitInput = value
                    },
                )
            }
        })

        if (!addCustomItem) {
            ContentRow(content = {
                Box(
                    modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                ) {
                    SelectionButton(
                        label = "Add Selection", action = {
                            viewModel.addLogFromSelection(quantityInput, unitInput)
                            popBackStack()
                        }, enabled = selectionButtonEnabled
                    )
                }
            })
        } else {
            // custom inputs
            ContentRow(content = {
                Column(
                    modifier = paddingModifier.weight(2f)
                ) {
                    CustomSourceInput(
                        label = "Source:",
                        value = sourceInput,
                        setValue = { value -> sourceInput = value }
                    )
                }

                Column(
                    modifier = paddingModifier.weight(2f)
                ) {
                    QuantityInput(
                        label = "Grams Protein / Serving:",
                        value = proteinInput,
                        setValue = { newValue ->
                            proteinInput = newValue
                        }
                    )
                }
            })

            StoreCustomOption(setStoreCustom = { storeCustom = it })

            // custom submit
            ContentRow(content = {
                Box(
                    modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                ) {
                    SelectionButton(
                        label = "Add Custom Item", action = {
                            // check if we are trying to save it
                            // if so, prompt store modal
                            if (storeCustom) {
                                viewModel.buildCustomSource(unitInput, quantityInput, sourceInput, proteinInput)
                            } else {
                                // otherwise add the custom log and popBack
                                addCustomLog()
                                popBackStack()
                            }

                        }, enabled = customButtonEnabled
                    )
                }
            })
        }

        // if new custom source exists then load add source dialog
        newCustomSource?.let { newSource ->
            ConfirmDialog(
                newSource,
                onConfirm = {
                    viewModel.storeCustomItem()
                    addCustomLog()
                    popBackStack()
                },
                onDismiss = {
                    addCustomLog()
                    popBackStack()
                }
            )
        }

        if (showDateDialog) {
            DatePickerModal(currentDate = localDateToEpochMilli(selectedDate),
                onDateSelected = { selected ->
                    viewModel.dateSelected(selected)
                },
                onDismiss = { showDateDialog = false })
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
fun QuantityInput(label: String, value: String, setValue: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = { newVal ->
            val trimmedValue = newVal.trimStart { it == '0' }
            setValue(trimmedValue)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}

@Composable
fun CustomSourceInput(label: String, value: String, setValue: (String) -> Unit) {
    TextField(
        value = value,

        onValueChange = { newVal ->
            setValue(newVal)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    )
}

@Composable
fun StoreCustomOption(setStoreCustom: (Boolean) -> Unit) {
    var checked by remember { mutableStateOf(true) }
    ContentRow(content = {
        Text("Remember Custom Item")
        Checkbox(checked = checked, onCheckedChange = {
            checked = it
            setStoreCustom(it)
        })
    })
}

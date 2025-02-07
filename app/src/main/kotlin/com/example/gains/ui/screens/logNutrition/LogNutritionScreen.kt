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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.example.gains.features.nutrition.Util.CUSTOM
import com.example.gains.features.nutrition.Util.formatDate
import com.example.gains.features.nutrition.Util.localDateToEpochMilli
import com.example.gains.ui.common.DatePickerModal
import com.example.gains.ui.common.DropdownSelector
import com.example.gains.ui.common.InputField
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar
import com.example.gains.ui.common.SelectionButton

@Composable
fun LogNutritionScreen(popBackStack: () -> Unit) {
    NavBar(
        title = "Log Protein",
        scrollContent = { paddingValues ->
            LogNutritionContent(paddingValues, popBackStack)
        },
        optionalNavigationComponent = { NavBackIcon(popBackStack) }
    )
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

    var state by remember { mutableStateOf(LogNutritionState()) }

    val customButtonEnabled by remember {
        derivedStateOf {
            state.quantity.toFloatOrNull()?.let { it > 0F } == true &&
                    state.protein.toFloatOrNull()?.let { it > 0F } == true &&
                    state.source.isNotEmpty()
        }
    }

    val selectionButtonEnabled by remember {
        derivedStateOf {
            state.quantity.toFloatOrNull()?.let { it > 0F } == true &&
                    sourceData != null
        }
    }

    // ensure source list is loaded
    LaunchedEffect(Unit) { viewModel.fetchSourceList(localContext) }

    var addCustomItem by remember { mutableStateOf(true) } // may just use source to check

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .clickable(
                indication = null, // Removes the ripple effect
                interactionSource = remember { MutableInteractionSource() } // Disables interaction tracking
            ) { focusManager.clearFocus() } // close keyboards on click
    ) {
        ContentRow {
            Text("Logging Protein for ${formatDate(selectedDate)}")
            IconButton(onClick = { state = state.copy(showDateDialog = true) }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }

        ContentRow(content = {
            Column(
                modifier = paddingModifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
                InputField(
                    label = "Quantity:",
                    value = state.quantity,
                    onValueChange = { state = state.copy(quantity = it) },
                    keyboardType = KeyboardType.Number
                )
            }

            // TODO: If the selected source has a servingUnit of Serving, only allow Serving selection
            Column(
                modifier = paddingModifier.weight(2f)
            ) {
                DropdownSelector(
                    label = "Unit:",
                    options = viewModel.sizeUnitSelections,
                    setValue = { state = state.copy(unit = it) }
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
                            viewModel.addLogFromSelection(state.quantity, state.unit)
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
                    InputField("Source:", state.source, { state = state.copy(source = it) })
                }

                Column(
                    modifier = paddingModifier.weight(2f)
                ) {
                    InputField(
                        label = "Grams Protein / Serving:",
                        value = state.protein,
                        onValueChange = { state = state.copy(protein = it) },
                        keyboardType = KeyboardType.Number)
                }
            })

            StoreCustomOption { state = state.copy(storeCustom = it) }

            // custom submit
            ContentRow(content = {
                Box(
                    modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                ) {
                    SelectionButton(
                        label = "Add Custom Item",
                        action = {
                            if (state.storeCustom) {
                                viewModel.buildCustomSource(
                                    state.unit,
                                    state.quantity,
                                    state.source,
                                    state.protein
                                )
                            } else {
                                viewModel.addCustomLog(
                                    state.source,
                                    state.unit,
                                    state.quantity,
                                    state.protein
                                )
                                popBackStack()
                            }
                        },
                        enabled = customButtonEnabled
                    )
                }
            })
        }

        newCustomSource?.let { newSource ->
            ConfirmDialog(
                newSource,
                onConfirm = {
                    viewModel.storeCustomItem()
                    viewModel.addCustomLog(state.source, state.unit, state.quantity, state.protein)
                    popBackStack()
                },
                onDismiss = {
                    viewModel.addCustomLog(state.source, state.unit, state.quantity, state.protein)
                    popBackStack()
                }
            )
        }

        if (state.showDateDialog) {
            DatePickerModal(currentDate = localDateToEpochMilli(selectedDate),
                onDateSelected = { viewModel.dateSelected(it) },
                onDismiss = { state = state.copy(showDateDialog = false) }
            )
        }
    }
}

// TODO: See if any of these ContentRows can be removed
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

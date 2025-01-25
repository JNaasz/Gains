package com.example.gains.features.nutrition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogNutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
) : ViewModel() {
    private val _customButtonEnabled = MutableStateFlow(false)
    val customButtonEnabled: StateFlow<Boolean> = _customButtonEnabled
    private val _selectionButtonEnabled = MutableStateFlow(false)
    val selectionButtonEnabled: StateFlow<Boolean> = _selectionButtonEnabled
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog
    private var pendingLog: NutritionLog? = null

    val sizeUnits: List<String> = listOf(
        SizeUnit.G.symbol,
        SizeUnit.OZ.symbol,
        SizeUnit.SERVING.symbol
    )

    val sourceList: List<String> = createOptionsList()

    private var quantityInput: Float = 0F
    private var selectedSizeUnit: String = SizeUnit.G.symbol
    private var selectedFood: String = ""
    private var customFood: String = ""
    private var customProteinContent: Float = 0F

    fun setQuantityInput(quantity: String) {
        quantityInput = quantity.toFloatOrNull() ?: 0f
        setButtonsEnabled()
    }

    fun selectUnit(sizeUnit: String) {
        selectedSizeUnit = sizeUnit
        setButtonsEnabled()
    }

    fun selectFoodType(item: String) {
        if (sourceList.contains(item)) {
            selectedFood = item
        } else {
            customFood = item
        }
        setButtonsEnabled()
    }

    fun setCustomProteinContent(quantity: String) {
        customProteinContent = quantity.toFloatOrNull() ?: 0f
        setButtonsEnabled()
    }

    fun addSelectedItem() {
        val newLog = NutritionLog(
            date = LocalDate.now(),
            foodName = selectedFood,
            unit = selectedSizeUnit,
            size = quantityInput,
            protein = 0F
        )

        Log.i("LogNutritionViewModel", newLog.toString())

        // newLog.protein = calcProtein(newLog) call helper function to calc protein based on other vals
    }

    fun addCustomItem() {
        pendingLog = NutritionLog(
            date = LocalDate.now(),
            foodName = customFood,
            unit = selectedSizeUnit,
            size = quantityInput,
            protein = quantityInput * customProteinContent // multiply for total protein
        )

        onShowDialog()
    }

    private fun addPendingLog() {
        pendingLog?.let { log ->
            addLog(log)
            pendingLog = null
        }
    }

    private fun onShowDialog() {
        _showDialog.value = true
    }

    fun onDismissDialog() {
        _showDialog.value = false
        addPendingLog()
    }

    fun storeCustomItem() {
        // TODO: set up DB to store selections and add them
        Log.i("LogNutritionViewModel", "storeCustomItem")
        onDismissDialog()
    }

    private fun createOptionsList(): List<String> {
        // pull options form config file
        // eventually pull form spread sheet or DB
        // need to make a DB for this so it can be updated and modified from inside and outside the app
        return listOf("Custom", "Chicken", "Beef", "Ham")
    }

    private fun setButtonsEnabled() {
        _customButtonEnabled.value = quantityInput != 0F && customProteinContent != 0F && customFood != ""
        _selectionButtonEnabled.value = quantityInput != 0F && selectedFood != ""
    }

    private fun addLog(newLog: NutritionLog) {
        viewModelScope.launch {
            nutritionRepository.addLog(newLog)
        }
    }
}
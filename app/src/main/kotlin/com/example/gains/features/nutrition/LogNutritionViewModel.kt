package com.example.gains.features.nutrition

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import com.example.gains.features.nutrition.Util.CUSTOM
import com.example.gains.features.nutrition.Util.calculateProtein
import com.example.gains.features.nutrition.Util.getMatchedSelectionData
import com.example.gains.features.nutrition.Util.getSourceList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogNutritionViewModel @Inject constructor(
    application: Application,
    private val nutritionRepository: NutritionRepository,
) : AndroidViewModel(application) {
    private val _customButtonEnabled = MutableStateFlow(false)
    val customButtonEnabled: StateFlow<Boolean> = _customButtonEnabled

    private val _selectionButtonEnabled = MutableStateFlow(false)
    val selectionButtonEnabled: StateFlow<Boolean> = _selectionButtonEnabled

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    // track whether the user is adding a custom item
    private val _addCustomItem = MutableStateFlow(true)
    val addCustomItem: StateFlow<Boolean> = _addCustomItem

    // data for selected stored item
    private val _sourceData = MutableStateFlow<SourceItem?>(null)
    var sourceData: StateFlow<SourceItem?> = _sourceData

    val sourceList: List<String> = getSourceList(application)
    val sizeUnitSelections = Util.sizeUnits
    var selectedDate: LocalDate = LocalDate.now()
    var storeCustom = true // true by default
    private var customProteinContent: Float = 0F

    // initialize a new log
    private var newLog: NutritionLog = NutritionLog(
        date = LocalDate.now(),
        foodName = "",
        unit = SizeUnit.SERVING.symbol,
        size = 0F, // quantity
        protein = 0F
    )

    fun setLogQuantity(quantity: String) {
        newLog.size = quantity.toFloatOrNull() ?: 0f
        setButtonsEnabled()
    }

    fun setLogUnit(sizeUnit: String) {
        newLog.unit = sizeUnit
        setButtonsEnabled()
    }

    fun setSourceSelection(selection: String) {
        _addCustomItem.value = selection == CUSTOM
        _sourceData.value = getMatchedSelectionData(selection)
        setButtonsEnabled()
    }

    fun setCustomFoodName(item: String) {
        newLog.foodName = item
        setButtonsEnabled()
    }

    fun setCustomProteinContent(quantity: String) {
        customProteinContent = quantity.toFloatOrNull() ?: 0f
        setButtonsEnabled()
    }

    fun addCustomItem() {
        // either prompt store dialog or save item
        if (storeCustom) {
            onShowDialog()
        } else {
            addCustomLog()
        }
    }

    private fun onShowDialog() {
        _showDialog.value = true
    }

    fun onDismissDialog() {
        _showDialog.value = false
        addCustomLog()
    }

    private fun setButtonsEnabled() {
        _customButtonEnabled.value = newLog.size > 0F
                && customProteinContent > 0F
                && newLog.foodName != ""

        _selectionButtonEnabled.value = newLog.size > 0F
                && _sourceData.value != null
    }

    fun addLogFromSelection() {
        val source = _sourceData.value ?: return // Exit early if null (shouldn't happen)

        val calculatedProtein = calculateProtein(newLog, source)
        newLog.foodName = source.name
        newLog.protein = calculatedProtein

        addNewLog()
    }

    private fun addCustomLog() {
        newLog.protein = newLog.size * customProteinContent
        addNewLog()
    }

    private fun addNewLog() {
        viewModelScope.launch {
            nutritionRepository.addLog(newLog)
        }
    }

    fun storeCustomItem() {
        // TODO: set up DB to store selections and add them
        Log.i("LogNutritionViewModel", "storeCustomItem")
        onDismissDialog()
    }
}
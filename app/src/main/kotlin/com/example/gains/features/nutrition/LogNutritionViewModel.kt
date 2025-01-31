package com.example.gains.features.nutrition

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import com.example.gains.database.ProteinSource
import com.example.gains.features.nutrition.Util.CUSTOM
import com.example.gains.features.nutrition.Util.calculateProtein
import com.example.gains.features.nutrition.Util.epochMilliToLocalDate
import com.example.gains.features.nutrition.Util.getMatchedSelectionData
import com.example.gains.features.nutrition.Util.getSourceList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogNutritionViewModel @Inject constructor(
    application: Application,
    private val nutritionRepository: NutritionRepository,
) : AndroidViewModel(application) {
    // TODO: attempt to move custom source list?
    private val customSourceList: StateFlow<List<ProteinSource>> =
        nutritionRepository.getProteinSources()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sourceList = MutableStateFlow(emptyList<String>())
    val sourceList: StateFlow<List<String>> = _sourceList

    private val _customButtonEnabled = MutableStateFlow(false)
    val customButtonEnabled: StateFlow<Boolean> = _customButtonEnabled

    private val _selectionButtonEnabled = MutableStateFlow(false)
    val selectionButtonEnabled: StateFlow<Boolean> = _selectionButtonEnabled

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _addCustomItem = MutableStateFlow(true)
    val addCustomItem: StateFlow<Boolean> = _addCustomItem

    private val _sourceData = MutableStateFlow<ProteinSource?>(null)
    val sourceData: StateFlow<ProteinSource?> = _sourceData

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val sizeUnitSelections = Util.sizeUnits
    private var customProteinContent: Float = 0F

    // initialize a new log
    private var newLog: NutritionLog = NutritionLog(
        date = selectedDate.value,
        foodName = "",
        unit = SizeUnit.SERVING.symbol,
        size = 0F, // quantity
        protein = 0F
    )

    val newSource = ProteinSource(
        name = newLog.foodName,
        servingUnit = newLog.unit,
        servingSize = newLog.size,
        proteinPerServing = customProteinContent
    )

    fun fetchSourceList(context: Context) {
        viewModelScope.launch {
            customSourceList.collectLatest { customSources ->
                val defaultSourceSelections = nutritionRepository.getDefaultSelections(context)
                _sourceList.value = getSourceList(customSources, defaultSourceSelections)
            }
        }
    }

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

    fun addCustomItem(storeCustom: Boolean) {
        // either prompt store dialog or save item
        if (storeCustom) {
            onShowDialog()
        } else {
            addCustomLog()
        }
    }

    private fun onShowDialog() {
        val size = if (newLog.unit == SizeUnit.SERVING.symbol) {
            1F
        } else {
            newLog.size
        }
        // update newSource values
        newSource.name = newLog.foodName
        newSource.servingUnit = newLog.unit
        newSource.proteinPerServing = customProteinContent
        newSource.servingSize = size

        _showDialog.value = true
    }

    fun onDismissDialog() {
        _showDialog.value = false
        addCustomLog()
    }

    private fun setButtonsEnabled() {
        _customButtonEnabled.value = newLog.size > 0F
                && customProteinContent > 0F
                && newLog.foodName.isNotEmpty()

        _selectionButtonEnabled.value = newLog.size > 0F
                && _sourceData.value != null
    }

    fun addLogFromSelection() {
        val source = _sourceData.value ?: return // Exit early if null (shouldn't happen)

        val calculatedProtein = calculateProtein(newLog, source)
        newLog.foodName = source.name
        newLog.protein = calculatedProtein
        newLog.date = selectedDate.value

        addNewLog()
    }

    private fun addCustomLog() {
        newLog.date = selectedDate.value
        newLog.protein = if (newLog.unit == SizeUnit.SERVING.symbol) {
            // multiply by number of servings
            newLog.size * customProteinContent
        } else {
            customProteinContent
        }

        addNewLog()
    }

    private fun addNewLog() {
        viewModelScope.launch {
            nutritionRepository.addLog(newLog)
        }
    }

    fun storeCustomItem() {
        viewModelScope.launch {
            nutritionRepository.storeProteinSource(newSource)
        }

        onDismissDialog()
    }

    fun dateSelected(selectedDateMillis: Long?) {
        selectedDateMillis?.let {
            _selectedDate.value = epochMilliToLocalDate(it)
        } ?: Log.d("TAG", "dateSelected: No date selected")
    }
}
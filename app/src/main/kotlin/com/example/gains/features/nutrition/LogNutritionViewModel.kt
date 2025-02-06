package com.example.gains.features.nutrition

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import com.example.gains.database.ProteinSource
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

    private val _sourceData = MutableStateFlow<ProteinSource?>(null)
    val sourceData: StateFlow<ProteinSource?> = _sourceData

    private val _newSource = MutableStateFlow<ProteinSource?>(null)
    var newSource: StateFlow<ProteinSource?> = _newSource

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val sizeUnitSelections = Util.sizeUnits

    fun fetchSourceList(context: Context) {
        viewModelScope.launch {
            customSourceList.collectLatest { customSources ->
                val defaultSourceSelections = nutritionRepository.getDefaultSelections(context)
                _sourceList.value = getSourceList(customSources, defaultSourceSelections)
            }
        }
    }

    fun setSourceSelection(selection: String) {
        _sourceData.value = getMatchedSelectionData(selection)
    }

    fun buildCustomSource(
        unitInput: String,
        quantityInput: String,
        sourceInput: String,
        proteinInput: String
    ) {
        val size = if (unitInput == SizeUnit.SERVING.symbol) {
            1F
        } else {
            quantityInput.toFloatOrNull() ?: 0F
        }

        _newSource.value = ProteinSource(
            name = sourceInput,
            servingUnit = unitInput,
            servingSize = size,
            proteinPerServing = proteinInput.toFloatOrNull() ?: 0F,
        )
    }

    fun addLogFromSelection(quantityInput: String, unitInput: String) {
        val source = _sourceData.value ?: return // Exit early if null (shouldn't happen)

        val quantity = quantityInput.toFloatOrNull() ?: 0F // Extracted from input
        if (quantity <= 0F) return // Prevent invalid entries

        val newLog = NutritionLog(
            date = selectedDate.value,
            foodName = source.name,
            unit = unitInput,
            size = quantity, // ✅ Set the actual quantity
            protein = 0F
        )

        newLog.protein = calculateProtein(newLog, source) // ✅ Now it calculates correctly

        addNewLog(newLog)
    }

    fun addCustomLog(
        sourceInput: String,
        unitInput: String,
        quantityInput: String,
        proteinInput: String
    ) {
        val newLog = NutritionLog(
            date = selectedDate.value,
            foodName = sourceInput,
            unit = unitInput,
            size = quantityInput.toFloatOrNull() ?: 0F,
            protein = 0F
        )

        val customProteinContent = proteinInput.toFloatOrNull() ?: 0F
        newLog.protein = if (newLog.unit == SizeUnit.SERVING.symbol) {
            // multiply by number of servings
            newLog.size * customProteinContent
        } else {
            customProteinContent
        }

        addNewLog(newLog)
    }

    private fun addNewLog(newLog: NutritionLog) {
        viewModelScope.launch {
            nutritionRepository.addLog(newLog)
        }
    }

    fun storeCustomItem() {
        viewModelScope.launch {
            val source = newSource.value
            if (source != null) {
                nutritionRepository.storeProteinSource(source)
            }
        }
    }

    fun dateSelected(selectedDateMillis: Long?) {
        selectedDateMillis?.let {
            _selectedDate.value = epochMilliToLocalDate(it)
        } ?: Log.d("TAG", "dateSelected: No date selected")
    }
}
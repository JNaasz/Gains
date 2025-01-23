package com.example.gains.features.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LogNutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
) : ViewModel() {
    private val date: LocalDate = LocalDate.now()

    val sizeUnits: List<String> = listOf(
        SizeUnit.G.toString(),
        SizeUnit.OZ.toString(),
        SizeUnit.SERVING.toString()
    )
    private var selectedSizeUnit: SizeUnit = SizeUnit.G
    var quantityInput: Float = 0F

    fun addLog(newLog: NutritionLog) {
        viewModelScope.launch {
            nutritionRepository.addLog(newLog)
        }
    }

    fun selectUnit(sizeUnit: String) {
        selectedSizeUnit = try {
            SizeUnit.valueOf(sizeUnit)
        } catch (e: IllegalArgumentException) {
            SizeUnit.G // always default to grams
        }
    }
}
package com.example.gains.features.nutrition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogNutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
) : ViewModel() {
    val sizeUnits: List<String> = listOf(
        SizeUnit.G.symbol,
        SizeUnit.OZ.symbol,
        SizeUnit.SERVING.symbol
    )

    val sourceList: List<String> = createOptionsList()

    private var selectedSizeUnit: String = SizeUnit.G.symbol
    private var selectedFood: String = ""

    var quantityInput: Float = 0F
    var customProteinContent: Float = 0F

    fun addLog(newLog: NutritionLog) {
        // build log from selections/inputs
        // then send to DB
        // private val date: LocalDate = LocalDate.now()
        viewModelScope.launch {
            nutritionRepository.addLog(newLog)
        }
    }

    fun selectUnit(sizeUnit: String) {
        selectedSizeUnit = sizeUnit
        Log.i("selected unit:", "selectUnit: $sizeUnit, $selectedSizeUnit")
    }

    fun selectFoodType(food: String) {
        // custom vs from sourceList, may want to have to variables
        selectedFood = food
    }

    private fun createOptionsList(): List<String> {
        // pull options form config file
        // eventually pull form spread sheet or DB
        // need to make a DB for this so it can be updated and modified from inside and outside the app
        return listOf("Custom", "Chicken", "Beef", "Ham")
    }
}
package com.example.gains.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.features.nutrition.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val date = LocalDate.now()

    val proteinTotal: StateFlow<Float> = nutritionRepository.getProteinTotal(date)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0F)

    fun getProteinTarget(): Int {
        return nutritionRepository.getProteinTarget()
    }
}
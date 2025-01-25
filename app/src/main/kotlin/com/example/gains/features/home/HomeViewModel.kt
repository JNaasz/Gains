package com.example.gains.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import com.example.gains.features.nutrition.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import java.math.BigDecimal

@HiltViewModel
class HomeViewModel @Inject constructor(
    nutritionRepository: NutritionRepository
) : ViewModel() {

    private val date = LocalDate.now()
    private val nutritionLogs: StateFlow<List<NutritionLog>> = nutritionRepository.getLogs(date)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val proteinTotal: StateFlow<BigDecimal> = nutritionLogs
        .map { logs ->
            logs.sumOf { it.protein.toBigDecimal() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BigDecimal.ZERO) // Default value as BigDecimal.ZERO
}
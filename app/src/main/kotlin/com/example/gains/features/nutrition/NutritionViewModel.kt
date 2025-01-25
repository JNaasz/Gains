package com.example.gains.features.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {
    private val date: LocalDate = LocalDate.now()

    val nutritionLogs: StateFlow<List<NutritionLog>> = nutritionRepository.getLogs(date)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // stateIn converts the flow to a stateFlow
    // when component is no longer needed it will stop collecting automatically
    // while subscribed means it will only collect while something is subscribed and stop collecting 5 seconds after collection ends

    fun deleteLog(log: NutritionLog) {
        viewModelScope.launch {
            nutritionRepository.deleteLog(log)
        }
    }
}
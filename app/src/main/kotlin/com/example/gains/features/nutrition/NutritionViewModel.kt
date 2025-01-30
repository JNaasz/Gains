package com.example.gains.features.nutrition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.NutritionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {
    private val today = LocalDate.now()
    private var dateModifier: Long = 0

    private val _date = MutableStateFlow<LocalDate>(today)
    val date: StateFlow<LocalDate> = _date

    private val _forwardEnabled = MutableStateFlow(false)
    val forwardEnabled: StateFlow<Boolean> = _forwardEnabled

    @OptIn(ExperimentalCoroutinesApi::class)
    val nutritionLogs: StateFlow<List<NutritionLog>> = _date
        .flatMapLatest { selectedDate ->
            Log.d("TAG", "Selected Date changed, pull logs for $selectedDate ")
            nutritionRepository.getLogs(selectedDate)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentProteinTotal: StateFlow<Float> = _date
        .flatMapLatest { selectedDate ->
            nutritionRepository.getProteinTotal(selectedDate)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0F)

    fun deleteLog(log: NutritionLog) {
        viewModelScope.launch {
            nutritionRepository.deleteLog(log)
        }
    }

    fun changeDate(change: Int) {
        dateModifier += change
        _date.value = today.plusDays(dateModifier)
        Log.d("TAG", "changeDate: dateModifier: $dateModifier")
        _forwardEnabled.value = dateModifier.toInt() < 0
    }
}
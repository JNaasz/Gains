package com.example.gains.features.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gains.database.ProteinSource
import com.example.gains.features.nutrition.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionSettingsViewModel @Inject constructor(
    application: Application,
    private val nutritionRepository: NutritionRepository,
) : AndroidViewModel(application) {
    private val _customSourceList = MutableStateFlow(emptyList<ProteinSource>())
    val customSourceList: StateFlow<List<ProteinSource>> = _customSourceList

    fun fetchSourceList() {
        viewModelScope.launch {
            nutritionRepository.getCustomProteinSources(viewModelScope).collect { customSources ->
                _customSourceList.value = customSources
            }
        }
    }

    fun getProteinGoal(): Int {
        return nutritionRepository.getProteinTarget()
    }

    fun setProteinGoal(target: Int) {
        nutritionRepository.setProteinTarget(target)
    }

}
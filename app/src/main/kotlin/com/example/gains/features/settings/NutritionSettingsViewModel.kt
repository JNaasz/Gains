package com.example.gains.features.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gains.features.nutrition.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NutritionSettingsViewModel @Inject constructor(
    application: Application,
    private val nutritionRepository: NutritionRepository,
) : AndroidViewModel(application) {

    fun getProteinGoal(): Int {
        return nutritionRepository.getProteinTarget()
    }

    fun setProteinGoal(target: Int) {
        nutritionRepository.setProteinTarget(target)
    }
}
package com.example.gains.ui.screens.logNutrition

import com.example.gains.features.nutrition.SizeUnit

data class LogNutritionState(
    var quantity: String = "0",
    var unit: String = SizeUnit.SERVING.symbol,
    var source: String = "",
    var protein: String = "0",
    var storeCustom: Boolean = true,
    var showDateDialog: Boolean = false
)

package com.example.gains.ui.navigation

sealed class NavRoute(val path: String) {

    data object Nutrition: NavRoute("nutrition")

    data object LogNutrition: NavRoute("log-nutrition")

    data object Home: NavRoute("home")

    data object NutritionSettings: NavRoute("nutrition-settings")
}

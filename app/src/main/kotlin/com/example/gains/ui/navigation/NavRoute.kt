package com.example.gains.ui.navigation

sealed class NavRoute(val path: String) {

    object Nutrition: NavRoute("nutrition")

    object LogNutrition: NavRoute("log-nutrition")

    object Home: NavRoute("home")
}

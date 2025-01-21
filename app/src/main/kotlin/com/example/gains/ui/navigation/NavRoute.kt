package com.example.gains.ui.navigation

sealed class NavRoute(val path: String) {

    object Nutrition: NavRoute("nutrition")

    object Home: NavRoute("home")
}

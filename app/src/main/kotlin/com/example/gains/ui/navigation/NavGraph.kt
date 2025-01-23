package com.example.gains.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gains.ui.screens.LogNutritionScreen
import com.example.gains.ui.screens.HomeScreen
import com.example.gains.ui.screens.NutritionScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.path
    ) {

        addHomeScreen(navController, this)

        addNutritionScreen(navController, this)

        addLogNutritionScreen(navController, this)
    }
}

private fun addHomeScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Home.path) {

        HomeScreen(
            navigateToNutrition = {
                navController.navigate(NavRoute.Nutrition.path)
            },
        )
    }
}

private fun addNutritionScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Nutrition.path) {

        NutritionScreen(
            popBackStack = { navController.popBackStack() },
            navToLogNutrition = {
                navController.navigate(NavRoute.LogNutrition.path)
            }
        )
    }
}

private fun addLogNutritionScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.LogNutrition.path) {

        LogNutritionScreen(
            popBackStack = { navController.popBackStack() },
        )
    }
}

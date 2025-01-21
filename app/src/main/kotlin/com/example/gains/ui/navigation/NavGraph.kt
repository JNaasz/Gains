package com.example.gains.ui.navigation

import LogNutritionScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gains.ui.screens.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.path
    ) {

        addHomeScreen(navController, this)

        addNutritionScreen(navController, this)
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

        LogNutritionScreen(
            // currently only pops back to home screen
            popBackStack = { navController.popBackStack() },
        )
    }
}

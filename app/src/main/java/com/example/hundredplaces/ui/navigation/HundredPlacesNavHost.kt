package com.example.hundredplaces.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hundredplaces.ui.home.HomeScreen
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.places.PlacesScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun HundredPlacesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PlacesDestination.route,
        modifier = modifier
        ) {
        composable(
            route = PlacesDestination.route
        ) {
            PlacesScreen()
        }
    }
}
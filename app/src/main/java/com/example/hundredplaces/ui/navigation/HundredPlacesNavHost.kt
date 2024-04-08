package com.example.hundredplaces.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hundredplaces.ui.AppContentType
import com.example.hundredplaces.ui.account.AccountDestination
import com.example.hundredplaces.ui.account.AccountScreen
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.achievements.AchievementsScreen
import com.example.hundredplaces.ui.map.MapDestination
import com.example.hundredplaces.ui.map.MapScreen
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.places.PlacesScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun HundredPlacesNavHost(
    navController: NavHostController,
    contentType: AppContentType,
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
        composable(
            route = MapDestination.route
        ) {
            MapScreen()
        }
        composable(
            route = AchievementsDestination.route
        ) {
            AchievementsScreen()
        }
        composable(
            route = AccountDestination.route
        ) {
            AccountScreen()
        }
    }
}
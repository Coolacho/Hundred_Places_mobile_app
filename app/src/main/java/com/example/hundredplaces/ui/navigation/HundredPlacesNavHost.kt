package com.example.hundredplaces.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hundredplaces.ui.AppContentType
import com.example.hundredplaces.ui.account.AccountDestination
import com.example.hundredplaces.ui.account.AccountScreen
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.achievements.AchievementsScreen
import com.example.hundredplaces.ui.map.MapDestination
import com.example.hundredplaces.ui.map.MapScreen
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.places.PlacesScreen
import com.example.hundredplaces.ui.places.details.PlaceDetailsDestination
import com.example.hundredplaces.ui.places.details.PlaceDetailsScreen

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
            PlacesScreen(
                contentType = contentType,
                navigateToPlaceEntry = {
                    navController.navigate("${PlaceDetailsDestination.route}/${it}")}
            )
        }
        composable(
            route = PlaceDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(PlaceDetailsDestination.PLACE_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            PlaceDetailsScreen(
                isFullScreen = false,
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = MapDestination.route
        ) {
            MapScreen(
                navigateToPlaceEntry = {
                    navController.navigate("${PlaceDetailsDestination.route}/${it}")}
            )
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
package com.example.hundredplaces.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.account.AccountDestination
import com.example.hundredplaces.ui.account.AccountScreen
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.account.CreateAccountDestination
import com.example.hundredplaces.ui.account.CreateAccountScreen
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.achievements.AchievementsScreen
import com.example.hundredplaces.ui.account.LoginDestination
import com.example.hundredplaces.ui.account.LoginScreen
import com.example.hundredplaces.ui.map.MapDestination
import com.example.hundredplaces.ui.map.MapScreen
import com.example.hundredplaces.ui.map.MapViewModel
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.places.PlacesViewModel
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsDestination
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsScreen
import com.example.hundredplaces.ui.places.PlacesScreenV2

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun HundredPlacesNavHost(
    startDestination: String,
    navController: NavHostController,
    accountViewModel: AccountViewModel,
    accountUiState: AccountUiState,
    placesViewModel: PlacesViewModel,
    placeDetailsViewModel: PlaceDetailsViewModel,
    mapViewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (accountUiState.isLoggedIn) startDestination else LoginDestination.route,
        modifier = modifier
        ) {
        composable(
            route = PlacesDestination.route
        ) {
            PlacesScreenV2(
                placesDetailsViewModel = placeDetailsViewModel,
                placesViewModel = placesViewModel
            )
        }
        composable(
            route = PlaceDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(PlaceDetailsDestination.PLACE_ID_ARG) {
                type = NavType.LongType
            })
        ) {
            PlaceDetailsScreen(
                isFullScreen = false,
                placesDetailsViewModel = placeDetailsViewModel,
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = MapDestination.route
        ) {
            MapScreen(
                mapViewModel = mapViewModel,
                navigateToPlaceEntry = {
                    placeDetailsViewModel.setPlaceId(it)
                    navController.navigate("${PlaceDetailsDestination.route}/${it}")}
            )
        }
        composable(
            route = AchievementsDestination.route
        ) {
            AchievementsScreen(
                achievementsViewModel = viewModel (
                    factory = AppViewModelProvider.Factory
                )
            )
        }
        composable(
            route = AccountDestination.route
        ) {
            AccountScreen(
                uiState = accountUiState,
                viewModel = accountViewModel,
                navigateToLogIn = {navController.navigate(LoginDestination.route)}
            )
        }
        composable(
            route = LoginDestination.route
        ) {
            LoginScreen(
                uiState = accountUiState,
                viewModel = accountViewModel,
                navigateToHome = { navController.navigate(PlacesDestination.route) },
                navigateToCreateAccount = { navController.navigate(CreateAccountDestination.route) },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        composable(
            route = CreateAccountDestination.route
        ) {
            CreateAccountScreen(
                uiState = accountUiState,
                viewModel = accountViewModel,
                navigateToHome = { navController.navigate(PlacesDestination.route) },
                navigateToLogin = { navController.navigate(LoginDestination.route) })
        }
    }
}
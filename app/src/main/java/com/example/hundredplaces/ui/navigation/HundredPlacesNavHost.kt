package com.example.hundredplaces.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.hundredplaces.ui.AppViewModelProvider
import com.example.hundredplaces.ui.account.AccountDestination
import com.example.hundredplaces.ui.account.AccountScreen
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.account.CreateAccountDestination
import com.example.hundredplaces.ui.account.CreateAccountScreen
import com.example.hundredplaces.ui.account.LoginDestination
import com.example.hundredplaces.ui.account.LoginScreen
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.achievements.AchievementsScreen
import com.example.hundredplaces.ui.camera.CameraScreen
import com.example.hundredplaces.ui.camera.CameraScreenDestination
import com.example.hundredplaces.ui.map.MapDestination
import com.example.hundredplaces.ui.map.MapScreen
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsDestination
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsScreen
import com.example.hundredplaces.ui.places.PlacesDestination
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
                onCameraButtonClick = {navController.navigate(CameraScreenDestination.route)},
            )
        }
        composable(
            route = PlaceDetailsDestination.routeWithArgs,
            deepLinks = listOf(navDeepLink<Long>(basePath = "http://192.168.2.150:8080/places/{placeId}")),
            arguments = listOf(navArgument(PlaceDetailsDestination.PLACE_ID_ARG) {
                type = NavType.LongType
            })
        ) { navBackStackEntry ->

            val placeId = navBackStackEntry.arguments?.getLong(PlaceDetailsDestination.PLACE_ID_ARG)
            val defaultExtras = (navBackStackEntry as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras ?: CreationExtras.Empty

            placeId?.let {
                PlaceDetailsScreen(
                    isFullScreen = false,
                    placesDetailsViewModel = viewModel(
                        factory = AppViewModelProvider.Factory,
                        extras = MutableCreationExtras(defaultExtras).apply {
                            set(AppViewModelProvider.PLACE_ID_KEY, placeId)
                        }
                    ),
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
        composable(
            route = MapDestination.route
        ) {
            MapScreen(
                navigateToPlaceEntry = { navController.navigate("${PlaceDetailsDestination.route}/${it}") }
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
        composable(
            route = CameraScreenDestination.route
        ) {
            CameraScreen()
        }
    }
}
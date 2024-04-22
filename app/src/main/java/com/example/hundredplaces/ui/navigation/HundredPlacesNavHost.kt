package com.example.hundredplaces.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hundredplaces.ui.AppContentType
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
import com.example.hundredplaces.ui.places.PlacesDestination
import com.example.hundredplaces.ui.places.PlacesScreen
import com.example.hundredplaces.ui.places.PlacesViewModel
import com.example.hundredplaces.ui.places.PlaceDetailsDestination
import com.example.hundredplaces.ui.places.PlaceDetailsScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun HundredPlacesNavHost(
    startDestination: String,
    navController: NavHostController,
    contentType: AppContentType,
    sharedAccountViewModel: AccountViewModel,
    sharedAccountUiState: AccountUiState,
    modifier: Modifier = Modifier,
    sharedPlacesViewModel: PlacesViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
) {
    val sharedPlacesUiState = sharedPlacesViewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = if (sharedAccountUiState.isLoggedIn) startDestination else LoginDestination.route,
        modifier = modifier
        ) {
        composable(
            route = PlacesDestination.route
        ) {
            PlacesScreen(
                contentType = contentType,
                accountUiState = sharedAccountUiState,
                viewModel = sharedPlacesViewModel,
                placesUiState = sharedPlacesUiState.value,
                navigateToPlaceEntry = {
                    navController.navigate("${PlaceDetailsDestination.route}/${it}")}
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
                accountUiState = sharedAccountUiState,
                viewModel = sharedPlacesViewModel,
                placesUiState = sharedPlacesUiState.value,
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = MapDestination.route
        ) {
            MapScreen(
                uiState = sharedPlacesUiState.value,
                navigateToPlaceEntry = {
                    navController.navigate("${PlaceDetailsDestination.route}/${it}")}
            )
        }
        composable(
            route = AchievementsDestination.route
        ) {
            AchievementsScreen(
                uiState = sharedAccountUiState
            )
        }
        composable(
            route = AccountDestination.route
        ) {
            AccountScreen(
                uiState = sharedAccountUiState,
                viewModel = sharedAccountViewModel,
                navigateToLogIn = {navController.navigate(LoginDestination.route)}
            )
        }
        composable(
            route = LoginDestination.route
        ) {
            LoginScreen(
                uiState = sharedAccountUiState,
                viewModel = sharedAccountViewModel,
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
                uiState = sharedAccountUiState,
                viewModel = sharedAccountViewModel,
                navigateToHome = { navController.navigate(PlacesDestination.route) },
                navigateToLogin = { navController.navigate(LoginDestination.route) })
        }
    }
}
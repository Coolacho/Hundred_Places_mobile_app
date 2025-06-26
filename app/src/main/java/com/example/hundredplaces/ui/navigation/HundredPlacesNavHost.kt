package com.example.hundredplaces.ui.navigation

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
import com.example.hundredplaces.ui.achievements.AchievementsDestination
import com.example.hundredplaces.ui.achievements.AchievementsScreen
import com.example.hundredplaces.ui.camera.CameraScreen
import com.example.hundredplaces.ui.camera.CameraScreenDestination
import com.example.hundredplaces.ui.camera.CameraUseCaseEnum
import com.example.hundredplaces.ui.login.LoginDestination
import com.example.hundredplaces.ui.login.LoginScreen
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
    userId: Long?,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (userId != null) startDestination else LoginDestination.route,
        modifier = modifier
        ) {
        composable(
            route = PlacesDestination.route
        ) {
            PlacesScreenV2(
                onCameraButtonClick = {navController.navigate("${CameraScreenDestination.route}/${CameraUseCaseEnum.LANDMARK.name}")},
            )
        }
        composable(
            route = PlaceDetailsDestination.routeWithArgs,
            deepLinks = listOf(navDeepLink<Long>(basePath = "http://10.0.2.2:5173/places/{${PlaceDetailsDestination.PLACE_ID_ARG}}?addVisit={${PlaceDetailsDestination.ADD_VISIT_ARG}}")),
            arguments = listOf(
                navArgument(PlaceDetailsDestination.PLACE_ID_ARG) { type = NavType.LongType },
                navArgument(PlaceDetailsDestination.ADD_VISIT_ARG) { type = NavType.BoolType })
        ) { navBackStackEntry ->

            val placeId = navBackStackEntry.arguments?.getLong(PlaceDetailsDestination.PLACE_ID_ARG)
            val addVisit = navBackStackEntry.arguments?.getBoolean(PlaceDetailsDestination.ADD_VISIT_ARG) == true
            val defaultExtras = (navBackStackEntry as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras ?: CreationExtras.Empty

            placeId?.let {
                PlaceDetailsScreen(
                    isFullScreen = false,
                    addVisit = addVisit,
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
                navigateToPlaceEntry = { navController.navigate("${PlaceDetailsDestination.route}/${it}?addVisit=${false}") }
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
        composable(
            route = LoginDestination.route
        ) {
            LoginScreen(
                navigateToHome = { navController.navigate(PlacesDestination.route) },
            )
        }
        composable(
            route = CameraScreenDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CameraScreenDestination.USE_CASE_ARG) { type = NavType.StringType}
            )
        ) { navBackStackEntry ->

            val useCase = navBackStackEntry.arguments?.getString(CameraScreenDestination.USE_CASE_ARG)

            useCase?.let {
                CameraScreen(
                    navigateToPlace = { placeId, addVisit -> navController.navigate("${PlaceDetailsDestination.route}/${placeId}?addVisit=${addVisit}") },
                    useCase = CameraUseCaseEnum.valueOf(useCase),
                )
            }
        }
    }
}
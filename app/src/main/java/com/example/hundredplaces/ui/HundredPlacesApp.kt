package com.example.hundredplaces.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hundredplaces.ui.home.HomeScreen

@Composable
fun HundredPlacesApp(
    windowSize: WindowWidthSizeClass,
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    val navigationType: AppNavigationType
    val contentType: AppContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
            contentType = AppContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = AppNavigationType.NAVIGATION_RAIL
            contentType = AppContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = AppNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = AppContentType.LIST_AND_DETAIL
        }
        else -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
            contentType = AppContentType.LIST_ONLY
        }
    }

    HomeScreen(
        startDestination = startDestination,
        navController = navController,
        navigationType = navigationType,
        contentType = contentType,
        modifier = modifier
    )
}
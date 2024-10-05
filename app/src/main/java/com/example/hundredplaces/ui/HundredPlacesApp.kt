package com.example.hundredplaces.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.home.HomeScreen
import com.example.hundredplaces.ui.navigation.AppNavigationType
import com.example.hundredplaces.ui.places.PlacesScreenContentType
import com.example.hundredplaces.ui.places.PlacesViewModel

@Composable
fun HundredPlacesApp(
    windowSize: WindowWidthSizeClass,
    startDestination: String,
    placesViewModel: PlacesViewModel,
    accountViewModel: AccountViewModel,
    modifier: Modifier = Modifier
) {

    val navigationType: AppNavigationType
    val contentType: PlacesScreenContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
            contentType = PlacesScreenContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = AppNavigationType.NAVIGATION_RAIL
            contentType = PlacesScreenContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = AppNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = PlacesScreenContentType.LIST_AND_DETAIL
        }
        else -> {
            navigationType = AppNavigationType.BOTTOM_NAVIGATION
            contentType = PlacesScreenContentType.LIST_ONLY
        }
    }

    HomeScreen(
        startDestination = startDestination,
        navigationType = navigationType,
        contentType = contentType,
        accountViewModel = accountViewModel,
        placesViewModel = placesViewModel,
        modifier = modifier
    )
}
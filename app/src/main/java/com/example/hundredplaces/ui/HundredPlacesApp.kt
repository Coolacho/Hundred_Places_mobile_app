package com.example.hundredplaces.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.home.HomeScreen
import com.example.hundredplaces.ui.navigation.AppNavigationType
import com.example.hundredplaces.ui.navigation.NavigationDrawerContent

@Composable
fun HundredPlacesApp(
    windowSize: WindowWidthSizeClass,
    startDestination: String,
    accountViewModel: AccountViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    val accountUiState = accountViewModel.uiState.collectAsState()
    val navigationType: AppNavigationType = when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            AppNavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            AppNavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            AppNavigationType.PERMANENT_NAVIGATION_DRAWER
        }
        else -> {
            AppNavigationType.BOTTOM_NAVIGATION
        }
    }

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER && accountUiState.value.isLoggedIn) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    modifier = Modifier.width(240.dp)
                ) {
                    NavigationDrawerContent(
                        navController = navController,
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                    )
                }
            },
            modifier = modifier
        ) {
            HomeScreen(
                accountUiState = accountUiState.value,
                accountViewModel = accountViewModel,
                startDestination = startDestination,
                navController = navController,
                navigationType = navigationType
            )
        }
    }
    else {
        HomeScreen(
            accountUiState = accountUiState.value,
            accountViewModel = accountViewModel,
            startDestination = startDestination,
            navController = navController,
            navigationType = navigationType,
            modifier = modifier
        )
    }

}
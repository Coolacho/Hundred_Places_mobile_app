package com.example.hundredplaces.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.map.MapViewModel
import com.example.hundredplaces.ui.navigation.AppBottomNavigationBar
import com.example.hundredplaces.ui.navigation.AppNavigationRail
import com.example.hundredplaces.ui.navigation.AppNavigationType
import com.example.hundredplaces.ui.navigation.HundredPlacesNavHost
import com.example.hundredplaces.ui.placeDetails.PlaceDetailsViewModel
import com.example.hundredplaces.ui.places.PlacesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    accountUiState: AccountUiState,
    accountViewModel: AccountViewModel,
    placeDetailsViewModel: PlaceDetailsViewModel,
    startDestination: String,
    navController: NavHostController,
    navigationType: AppNavigationType,
    modifier: Modifier = Modifier,
) {
    Scaffold (
        topBar = {
            if (navigationType != AppNavigationType.PERMANENT_NAVIGATION_DRAWER || !accountUiState.isLoggedIn) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.displayLarge
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION && accountUiState.isLoggedIn) {
                AppBottomNavigationBar(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        contentWindowInsets = WindowInsets(0,0,0,0),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
            .padding(it)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = navigationType == AppNavigationType.NAVIGATION_RAIL && accountUiState.isLoggedIn) {
                    AppNavigationRail(
                        navController = navController,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HundredPlacesNavHost(
                        startDestination = startDestination,
                        navController = navController,
                        accountViewModel = accountViewModel,
                        accountUiState = accountUiState,
                        placeDetailsViewModel = placeDetailsViewModel,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
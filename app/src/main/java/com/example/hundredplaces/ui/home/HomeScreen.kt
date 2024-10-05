package com.example.hundredplaces.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hundredplaces.R
import com.example.hundredplaces.ui.places.PlacesScreenContentType
import com.example.hundredplaces.ui.navigation.AppNavigationType
import com.example.hundredplaces.ui.account.AccountUiState
import com.example.hundredplaces.ui.account.AccountViewModel
import com.example.hundredplaces.ui.navigation.AppBottomNavigationBar
import com.example.hundredplaces.ui.navigation.AppNavigationRail
import com.example.hundredplaces.ui.navigation.HundredPlacesNavHost
import com.example.hundredplaces.ui.navigation.NavigationDrawerContent
import com.example.hundredplaces.ui.places.PlacesViewModel

@Composable
fun HomeScreen(
    startDestination: String,
    navigationType: AppNavigationType,
    contentType: PlacesScreenContentType,
    placesViewModel: PlacesViewModel,
    accountViewModel: AccountViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val accountUiState = accountViewModel.uiState.collectAsState()

    if (navigationType == AppNavigationType.PERMANENT_NAVIGATION_DRAWER && accountUiState.value.isLoggedIn) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
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
            HomeScreenContent(
                accountUiState = accountUiState.value,
                accountViewModel = accountViewModel,
                placesViewModel = placesViewModel,
                startDestination = startDestination,
                navController = navController,
                contentType = contentType,
                navigationType = navigationType
            )
        }
    }
    else {
        HomeScreenContent(
            accountUiState = accountUiState.value,
            accountViewModel = accountViewModel,
            placesViewModel = placesViewModel,
            startDestination = startDestination,
            navController = navController,
            contentType = contentType,
            navigationType = navigationType,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    accountUiState: AccountUiState,
    accountViewModel: AccountViewModel,
    placesViewModel: PlacesViewModel,
    startDestination: String,
    navController: NavHostController,
    contentType: PlacesScreenContentType,
    navigationType: AppNavigationType,
    modifier: Modifier = Modifier,
) {
    Scaffold(
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
        modifier = modifier
    ) {paddingValues ->
        Box(
            modifier = Modifier
            .padding(paddingValues)
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
                        contentType = contentType,
                        accountViewModel = accountViewModel,
                        accountUiState = accountUiState,
                        placesViewModel = placesViewModel,
                        modifier = Modifier.weight(1f)
                    )
                    AnimatedVisibility(visible = navigationType == AppNavigationType.BOTTOM_NAVIGATION && accountUiState.isLoggedIn) {
                        AppBottomNavigationBar(
                            navController = navController,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}